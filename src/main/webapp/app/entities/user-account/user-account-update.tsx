import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './user-account.reducer';
import { IUserAccount } from 'app/shared/model/user-account.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserAccountUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IUserAccountUpdateState {
  isNew: boolean;
}

export class UserAccountUpdate extends React.Component<IUserAccountUpdateProps, IUserAccountUpdateState> {
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

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { userAccountEntity } = this.props;
      const entity = {
        ...userAccountEntity,
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
    this.props.history.push('/entity/user-account');
  };

  render() {
    const { userAccountEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="appApp.userAccount.home.createOrEditLabel">Create or edit a UserAccount</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : userAccountEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="user-account-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="activatedLabel" check>
                    <AvInput id="user-account-activated" type="checkbox" className="form-control" name="activated" />
                    Activated
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="recruiterLabel" check>
                    <AvInput id="user-account-recruiter" type="checkbox" className="form-control" name="recruiter" />
                    Recruiter
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="receiveMailingLabel" check>
                    <AvInput id="user-account-receiveMailing" type="checkbox" className="form-control" name="receiveMailing" />
                    Receive Mailing
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="user-account-name" type="text" name="name" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/user-account" replace color="info">
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
  userAccountEntity: storeState.userAccount.entity,
  loading: storeState.userAccount.loading,
  updating: storeState.userAccount.updating
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserAccountUpdate);
