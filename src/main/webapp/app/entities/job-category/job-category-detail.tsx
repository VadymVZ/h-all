import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './job-category.reducer';
import { IJobCategory } from 'app/shared/model/job-category.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IJobCategoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class JobCategoryDetail extends React.Component<IJobCategoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { jobCategoryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            JobCategory [<b>{jobCategoryEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="parentId">Parent Id</span>
            </dt>
            <dd>{jobCategoryEntity.parentId}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{jobCategoryEntity.name}</dd>
          </dl>
          <Button tag={Link} to="/entity/job-category" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/job-category/${jobCategoryEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ jobCategory }: IRootState) => ({
  jobCategoryEntity: jobCategory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(JobCategoryDetail);
