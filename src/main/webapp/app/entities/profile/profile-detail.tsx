import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProfileDetail extends React.Component<IProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { profileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Profile [<b>{profileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="salaryAmount">Salary Amount</span>
            </dt>
            <dd>{profileEntity.salaryAmount}</dd>
            <dt>
              <span id="city">City</span>
            </dt>
            <dd>{profileEntity.city}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{profileEntity.description}</dd>
            <dt>
              <span id="experience">Experience</span>
            </dt>
            <dd>{profileEntity.experience}</dd>
            <dt>
              <span id="jobExpectations">Job Expectations</span>
            </dt>
            <dd>{profileEntity.jobExpectations}</dd>
            <dt>
              <span id="achievements">Achievements</span>
            </dt>
            <dd>{profileEntity.achievements}</dd>
            <dt>
              <span id="positionName">Position Name</span>
            </dt>
            <dd>{profileEntity.positionName}</dd>
            <dt>
              <span id="y">Y</span>
            </dt>
            <dd>
              {profileEntity.y ? (
                <div>
                  <a onClick={openFile(profileEntity.yContentType, profileEntity.y)}>
                    <img src={`data:${profileEntity.yContentType};base64,${profileEntity.y}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {profileEntity.yContentType}, {byteSize(profileEntity.y)}
                  </span>
                </div>
              ) : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/profile/${profileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ profile }: IRootState) => ({
  profileEntity: profile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProfileDetail);
