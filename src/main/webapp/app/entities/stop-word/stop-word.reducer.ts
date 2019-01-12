import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStopWord, defaultValue } from 'app/shared/model/stop-word.model';

export const ACTION_TYPES = {
  FETCH_STOPWORD_LIST: 'stopWord/FETCH_STOPWORD_LIST',
  FETCH_STOPWORD: 'stopWord/FETCH_STOPWORD',
  CREATE_STOPWORD: 'stopWord/CREATE_STOPWORD',
  UPDATE_STOPWORD: 'stopWord/UPDATE_STOPWORD',
  DELETE_STOPWORD: 'stopWord/DELETE_STOPWORD',
  RESET: 'stopWord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStopWord>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type StopWordState = Readonly<typeof initialState>;

// Reducer

export default (state: StopWordState = initialState, action): StopWordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STOPWORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STOPWORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STOPWORD):
    case REQUEST(ACTION_TYPES.UPDATE_STOPWORD):
    case REQUEST(ACTION_TYPES.DELETE_STOPWORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STOPWORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STOPWORD):
    case FAILURE(ACTION_TYPES.CREATE_STOPWORD):
    case FAILURE(ACTION_TYPES.UPDATE_STOPWORD):
    case FAILURE(ACTION_TYPES.DELETE_STOPWORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STOPWORD_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STOPWORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STOPWORD):
    case SUCCESS(ACTION_TYPES.UPDATE_STOPWORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STOPWORD):
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

const apiUrl = 'api/stop-words';

// Actions

export const getEntities: ICrudGetAllAction<IStopWord> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STOPWORD_LIST,
    payload: axios.get<IStopWord>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IStopWord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STOPWORD,
    payload: axios.get<IStopWord>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStopWord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STOPWORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStopWord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STOPWORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStopWord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STOPWORD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
