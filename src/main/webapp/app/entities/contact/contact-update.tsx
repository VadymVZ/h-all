import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IContactUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IContactUpdateState {
  isNew: boolean;
}

export class ContactUpdate extends React.Component<IContactUpdateProps, IContactUpdateState> {
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
      const { contactEntity } = this.props;
      const entity = {
        ...contactEntity,
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
    this.props.history.push('/entity/contact');
  };

  render() {
    const { contactEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="appApp.contact.home.createOrEditLabel">Create or edit a Contact</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : contactEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="contact-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="contact-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="lastNameLabel" for="lastName">
                    Last Name
                  </Label>
                  <AvField id="contact-lastName" type="text" name="lastName" />
                </AvGroup>
                <AvGroup>
                  <Label id="skypeLabel" for="skype">
                    Skype
                  </Label>
                  <AvField id="contact-skype" type="text" name="skype" />
                </AvGroup>
                <AvGroup>
                  <Label id="githubLabel" for="github">
                    Github
                  </Label>
                  <AvField id="contact-github" type="text" name="github" />
                </AvGroup>
                <AvGroup>
                  <Label id="telegramLabel" for="telegram">
                    Telegram
                  </Label>
                  <AvField id="contact-telegram" type="text" name="telegram" />
                </AvGroup>
                <AvGroup>
                  <Label id="linkedinLabel" for="linkedin">
                    Linkedin
                  </Label>
                  <AvField id="contact-linkedin" type="text" name="linkedin" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    Email
                  </Label>
                  <AvField id="contact-email" type="text" name="email" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/contact" replace color="info">
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
  contactEntity: storeState.contact.entity,
  loading: storeState.contact.loading,
  updating: storeState.contact.updating
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
)(ContactUpdate);
