import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICvFile, defaultValue } from 'app/shared/model/cv-file.model';

export const ACTION_TYPES = {
  FETCH_CVFILE_LIST: 'cvFile/FETCH_CVFILE_LIST',
  FETCH_CVFILE: 'cvFile/FETCH_CVFILE',
  CREATE_CVFILE: 'cvFile/CREATE_CVFILE',
  UPDATE_CVFILE: 'cvFile/UPDATE_CVFILE',
  DELETE_CVFILE: 'cvFile/DELETE_CVFILE',
  SET_BLOB: 'cvFile/SET_BLOB',
  RESET: 'cvFile/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICvFile>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CvFileState = Readonly<typeof initialState>;

// Reducer

export default (state: CvFileState = initialState, action): CvFileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CVFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CVFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CVFILE):
    case REQUEST(ACTION_TYPES.UPDATE_CVFILE):
    case REQUEST(ACTION_TYPES.DELETE_CVFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CVFILE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CVFILE):
    case FAILURE(ACTION_TYPES.CREATE_CVFILE):
    case FAILURE(ACTION_TYPES.UPDATE_CVFILE):
    case FAILURE(ACTION_TYPES.DELETE_CVFILE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CVFILE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CVFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CVFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_CVFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CVFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/cv-files';

// Actions

export const getEntities: ICrudGetAllAction<ICvFile> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CVFILE_LIST,
    payload: axios.get<ICvFile>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICvFile> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CVFILE,
    payload: axios.get<ICvFile>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICvFile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CVFILE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICvFile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CVFILE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICvFile> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CVFILE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
