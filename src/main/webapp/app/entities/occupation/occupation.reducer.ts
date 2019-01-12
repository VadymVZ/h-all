import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOccupation, defaultValue } from 'app/shared/model/occupation.model';

export const ACTION_TYPES = {
  FETCH_OCCUPATION_LIST: 'occupation/FETCH_OCCUPATION_LIST',
  FETCH_OCCUPATION: 'occupation/FETCH_OCCUPATION',
  CREATE_OCCUPATION: 'occupation/CREATE_OCCUPATION',
  UPDATE_OCCUPATION: 'occupation/UPDATE_OCCUPATION',
  DELETE_OCCUPATION: 'occupation/DELETE_OCCUPATION',
  RESET: 'occupation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOccupation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OccupationState = Readonly<typeof initialState>;

// Reducer

export default (state: OccupationState = initialState, action): OccupationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_OCCUPATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OCCUPATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_OCCUPATION):
    case REQUEST(ACTION_TYPES.UPDATE_OCCUPATION):
    case REQUEST(ACTION_TYPES.DELETE_OCCUPATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_OCCUPATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OCCUPATION):
    case FAILURE(ACTION_TYPES.CREATE_OCCUPATION):
    case FAILURE(ACTION_TYPES.UPDATE_OCCUPATION):
    case FAILURE(ACTION_TYPES.DELETE_OCCUPATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_OCCUPATION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_OCCUPATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_OCCUPATION):
    case SUCCESS(ACTION_TYPES.UPDATE_OCCUPATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_OCCUPATION):
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

const apiUrl = 'api/occupations';

// Actions

export const getEntities: ICrudGetAllAction<IOccupation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_OCCUPATION_LIST,
    payload: axios.get<IOccupation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOccupation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OCCUPATION,
    payload: axios.get<IOccupation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOccupation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OCCUPATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOccupation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OCCUPATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOccupation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OCCUPATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
