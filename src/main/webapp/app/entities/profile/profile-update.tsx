import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProfileUpdateState {
  isNew: boolean;
}

export class ProfileUpdate extends React.Component<IProfileUpdateProps, IProfileUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { profileEntity } = this.props;
      const entity = {
        ...profileEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/profile');
  };

  render() {
    const { profileEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const { y, yContentType } = profileEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="appApp.profile.home.createOrEditLabel">Create or edit a Profile</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : profileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="profile-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="salaryAmountLabel" for="salaryAmount">
                    Salary Amount
                  </Label>
                  <AvField id="profile-salaryAmount" type="text" name="salaryAmount" />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="city">
                    City
                  </Label>
                  <AvField id="profile-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    Description
                  </Label>
                  <AvField id="profile-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="experienceLabel" for="experience">
                    Experience
                  </Label>
                  <AvField id="profile-experience" type="text" name="experience" />
                </AvGroup>
                <AvGroup>
                  <Label id="jobExpectationsLabel" for="jobExpectations">
                    Job Expectations
                  </Label>
                  <AvField id="profile-jobExpectations" type="text" name="jobExpectations" />
                </AvGroup>
                <AvGroup>
                  <Label id="achievementsLabel" for="achievements">
                    Achievements
                  </Label>
                  <AvField id="profile-achievements" type="text" name="achievements" />
                </AvGroup>
                <AvGroup>
                  <Label id="positionNameLabel" for="positionName">
                    Position Name
                  </Label>
                  <AvField id="profile-positionName" type="text" name="positionName" />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="yLabel" for="y">
                      Y
                    </Label>
                    <br />
                    {y ? (
                      <div>
                        <a onClick={openFile(yContentType, y)}>
                          <img src={`data:${yContentType};base64,${y}`} style={{ maxHeight: '100px' }} />
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {yContentType}, {byteSize(y)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('y')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_y" type="file" onChange={this.onBlobChange(true, 'y')} accept="image/*" />
                    <AvInput type="hidden" name="y" value={y} />
                  </AvGroup>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/profile" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  profileEntity: storeState.profile.entity,
  loading: storeState.profile.loading,
  updating: storeState.profile.updating
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProfileUpdate);
