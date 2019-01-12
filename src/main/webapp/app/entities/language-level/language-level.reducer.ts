import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILanguageLevel, defaultValue } from 'app/shared/model/language-level.model';

export const ACTION_TYPES = {
  FETCH_LANGUAGELEVEL_LIST: 'languageLevel/FETCH_LANGUAGELEVEL_LIST',
  FETCH_LANGUAGELEVEL: 'languageLevel/FETCH_LANGUAGELEVEL',
  CREATE_LANGUAGELEVEL: 'languageLevel/CREATE_LANGUAGELEVEL',
  UPDATE_LANGUAGELEVEL: 'languageLevel/UPDATE_LANGUAGELEVEL',
  DELETE_LANGUAGELEVEL: 'languageLevel/DELETE_LANGUAGELEVEL',
  RESET: 'languageLevel/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILanguageLevel>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type LanguageLevelState = Readonly<typeof initialState>;

// Reducer

export default (state: LanguageLevelState = initialState, action): LanguageLevelState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LANGUAGELEVEL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LANGUAGELEVEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LANGUAGELEVEL):
    case REQUEST(ACTION_TYPES.UPDATE_LANGUAGELEVEL):
    case REQUEST(ACTION_TYPES.DELETE_LANGUAGELEVEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LANGUAGELEVEL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LANGUAGELEVEL):
    case FAILURE(ACTION_TYPES.CREATE_LANGUAGELEVEL):
    case FAILURE(ACTION_TYPES.UPDATE_LANGUAGELEVEL):
    case FAILURE(ACTION_TYPES.DELETE_LANGUAGELEVEL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANGUAGELEVEL_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANGUAGELEVEL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LANGUAGELEVEL):
    case SUCCESS(ACTION_TYPES.UPDATE_LANGUAGELEVEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LANGUAGELEVEL):
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

const apiUrl = 'api/language-levels';

// Actions

export const getEntities: ICrudGetAllAction<ILanguageLevel> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LANGUAGELEVEL_LIST,
    payload: axios.get<ILanguageLevel>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ILanguageLevel> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LANGUAGELEVEL,
    payload: axios.get<ILanguageLevel>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILanguageLevel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LANGUAGELEVEL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILanguageLevel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LANGUAGELEVEL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILanguageLevel> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LANGUAGELEVEL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
