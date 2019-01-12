import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IJobCategory, defaultValue } from 'app/shared/model/job-category.model';

export const ACTION_TYPES = {
  FETCH_JOBCATEGORY_LIST: 'jobCategory/FETCH_JOBCATEGORY_LIST',
  FETCH_JOBCATEGORY: 'jobCategory/FETCH_JOBCATEGORY',
  CREATE_JOBCATEGORY: 'jobCategory/CREATE_JOBCATEGORY',
  UPDATE_JOBCATEGORY: 'jobCategory/UPDATE_JOBCATEGORY',
  DELETE_JOBCATEGORY: 'jobCategory/DELETE_JOBCATEGORY',
  RESET: 'jobCategory/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IJobCategory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type JobCategoryState = Readonly<typeof initialState>;

// Reducer

export default (state: JobCategoryState = initialState, action): JobCategoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_JOBCATEGORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_JOBCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_JOBCATEGORY):
    case REQUEST(ACTION_TYPES.UPDATE_JOBCATEGORY):
    case REQUEST(ACTION_TYPES.DELETE_JOBCATEGORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_JOBCATEGORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_JOBCATEGORY):
    case FAILURE(ACTION_TYPES.CREATE_JOBCATEGORY):
    case FAILURE(ACTION_TYPES.UPDATE_JOBCATEGORY):
    case FAILURE(ACTION_TYPES.DELETE_JOBCATEGORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_JOBCATEGORY_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_JOBCATEGORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_JOBCATEGORY):
    case SUCCESS(ACTION_TYPES.UPDATE_JOBCATEGORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_JOBCATEGORY):
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

const apiUrl = 'api/job-categories';

// Actions

export const getEntities: ICrudGetAllAction<IJobCategory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_JOBCATEGORY_LIST,
    payload: axios.get<IJobCategory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IJobCategory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_JOBCATEGORY,
    payload: axios.get<IJobCategory>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IJobCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_JOBCATEGORY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IJobCategory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_JOBCATEGORY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IJobCategory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_JOBCATEGORY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
