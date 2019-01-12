import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserAccount, defaultValue } from 'app/shared/model/user-account.model';

export const ACTION_TYPES = {
  FETCH_USERACCOUNT_LIST: 'userAccount/FETCH_USERACCOUNT_LIST',
  FETCH_USERACCOUNT: 'userAccount/FETCH_USERACCOUNT',
  CREATE_USERACCOUNT: 'userAccount/CREATE_USERACCOUNT',
  UPDATE_USERACCOUNT: 'userAccount/UPDATE_USERACCOUNT',
  DELETE_USERACCOUNT: 'userAccount/DELETE_USERACCOUNT',
  RESET: 'userAccount/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserAccount>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserAccountState = Readonly<typeof initialState>;

// Reducer

export default (state: UserAccountState = initialState, action): UserAccountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERACCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERACCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERACCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_USERACCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_USERACCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERACCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERACCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_USERACCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_USERACCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_USERACCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERACCOUNT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERACCOUNT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERACCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_USERACCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERACCOUNT):
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

const apiUrl = 'api/user-accounts';

// Actions

export const getEntities: ICrudGetAllAction<IUserAccount> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERACCOUNT_LIST,
    payload: axios.get<IUserAccount>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserAccount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERACCOUNT,
    payload: axios.get<IUserAccount>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserAccount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERACCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserAccount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERACCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserAccount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERACCOUNT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
