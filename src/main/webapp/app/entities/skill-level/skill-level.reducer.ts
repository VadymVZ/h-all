import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISkillLevel, defaultValue } from 'app/shared/model/skill-level.model';

export const ACTION_TYPES = {
  FETCH_SKILLLEVEL_LIST: 'skillLevel/FETCH_SKILLLEVEL_LIST',
  FETCH_SKILLLEVEL: 'skillLevel/FETCH_SKILLLEVEL',
  CREATE_SKILLLEVEL: 'skillLevel/CREATE_SKILLLEVEL',
  UPDATE_SKILLLEVEL: 'skillLevel/UPDATE_SKILLLEVEL',
  DELETE_SKILLLEVEL: 'skillLevel/DELETE_SKILLLEVEL',
  RESET: 'skillLevel/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISkillLevel>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SkillLevelState = Readonly<typeof initialState>;

// Reducer

export default (state: SkillLevelState = initialState, action): SkillLevelState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SKILLLEVEL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SKILLLEVEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SKILLLEVEL):
    case REQUEST(ACTION_TYPES.UPDATE_SKILLLEVEL):
    case REQUEST(ACTION_TYPES.DELETE_SKILLLEVEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SKILLLEVEL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SKILLLEVEL):
    case FAILURE(ACTION_TYPES.CREATE_SKILLLEVEL):
    case FAILURE(ACTION_TYPES.UPDATE_SKILLLEVEL):
    case FAILURE(ACTION_TYPES.DELETE_SKILLLEVEL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SKILLLEVEL_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SKILLLEVEL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SKILLLEVEL):
    case SUCCESS(ACTION_TYPES.UPDATE_SKILLLEVEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SKILLLEVEL):
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

const apiUrl = 'api/skill-levels';

// Actions

export const getEntities: ICrudGetAllAction<ISkillLevel> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SKILLLEVEL_LIST,
    payload: axios.get<ISkillLevel>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISkillLevel> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SKILLLEVEL,
    payload: axios.get<ISkillLevel>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISkillLevel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SKILLLEVEL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISkillLevel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SKILLLEVEL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISkillLevel> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SKILLLEVEL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
