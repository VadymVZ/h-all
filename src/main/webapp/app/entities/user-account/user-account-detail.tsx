import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-account.reducer';
import { IUserAccount } from 'app/shared/model/user-account.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserAccountDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserAccountDetail extends React.Component<IUserAccountDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userAccountEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            UserAccount [<b>{userAccountEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="activated">Activated</span>
            </dt>
            <dd>{userAccountEntity.activated ? 'true' : 'false'}</dd>
            <dt>
              <span id="recruiter">Recruiter</span>
            </dt>
            <dd>{userAccountEntity.recruiter ? 'true' : 'false'}</dd>
            <dt>
              <span id="receiveMailing">Receive Mailing</span>
            </dt>
            <dd>{userAccountEntity.receiveMailing ? 'true' : 'false'}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{userAccountEntity.name}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-account" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/user-account/${userAccountEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userAccount }: IRootState) => ({
  userAccountEntity: userAccount.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserAccountDetail);
