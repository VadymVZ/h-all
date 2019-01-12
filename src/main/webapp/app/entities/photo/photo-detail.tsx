import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './photo.reducer';
import { IPhoto } from 'app/shared/model/photo.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhotoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PhotoDetail extends React.Component<IPhotoDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { photoEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Photo [<b>{photoEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="value">Value</span>
            </dt>
            <dd>
              {photoEntity.value ? (
                <div>
                  <a onClick={openFile(photoEntity.valueContentType, photoEntity.value)}>
                    <img src={`data:${photoEntity.valueContentType};base64,${photoEntity.value}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {photoEntity.valueContentType}, {byteSize(photoEntity.value)}
                  </span>
                </div>
              ) : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/photo" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/photo/${photoEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ photo }: IRootState) => ({
  photoEntity: photo.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhotoDetail);
