import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEmployeeAccount, defaultValue } from 'app/shared/model/employee-account.model';

export const ACTION_TYPES = {
  FETCH_EMPLOYEEACCOUNT_LIST: 'employeeAccount/FETCH_EMPLOYEEACCOUNT_LIST',
  FETCH_EMPLOYEEACCOUNT: 'employeeAccount/FETCH_EMPLOYEEACCOUNT',
  CREATE_EMPLOYEEACCOUNT: 'employeeAccount/CREATE_EMPLOYEEACCOUNT',
  UPDATE_EMPLOYEEACCOUNT: 'employeeAccount/UPDATE_EMPLOYEEACCOUNT',
  DELETE_EMPLOYEEACCOUNT: 'employeeAccount/DELETE_EMPLOYEEACCOUNT',
  RESET: 'employeeAccount/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEmployeeAccount>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EmployeeAccountState = Readonly<typeof initialState>;

// Reducer

export default (state: EmployeeAccountState = initialState, action): EmployeeAccountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EMPLOYEEACCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EMPLOYEEACCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EMPLOYEEACCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_EMPLOYEEACCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_EMPLOYEEACCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EMPLOYEEACCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EMPLOYEEACCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_EMPLOYEEACCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_EMPLOYEEACCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_EMPLOYEEACCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMPLOYEEACCOUNT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EMPLOYEEACCOUNT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EMPLOYEEACCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_EMPLOYEEACCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EMPLOYEEACCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/employee-accounts';

// Actions

export const getEntities: ICrudGetAllAction<IEmployeeAccount> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EMPLOYEEACCOUNT_LIST,
    payload: axios.get<IEmployeeAccount>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEmployeeAccount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EMPLOYEEACCOUNT,
    payload: axios.get<IEmployeeAccount>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEmployeeAccount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EMPLOYEEACCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEmployeeAccount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EMPLOYEEACCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEmployeeAccount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EMPLOYEEACCOUNT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
