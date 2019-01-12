import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './cv-file.reducer';
import { ICvFile } from 'app/shared/model/cv-file.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICvFileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CvFileDetail extends React.Component<ICvFileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { cvFileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            CvFile [<b>{cvFileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="cv">Cv</span>
            </dt>
            <dd>
              {cvFileEntity.cv ? (
                <div>
                  <a onClick={openFile(cvFileEntity.cvContentType, cvFileEntity.cv)}>Open&nbsp;</a>
                  <span>
                    {cvFileEntity.cvContentType}, {byteSize(cvFileEntity.cv)}
                  </span>
                </div>
              ) : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/cv-file" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/cv-file/${cvFileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ cvFile }: IRootState) => ({
  cvFileEntity: cvFile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CvFileDetail);
