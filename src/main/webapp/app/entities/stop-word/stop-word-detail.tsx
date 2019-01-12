import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './stop-word.reducer';
import { IStopWord } from 'app/shared/model/stop-word.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStopWordDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StopWordDetail extends React.Component<IStopWordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { stopWordEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            StopWord [<b>{stopWordEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="key">Key</span>
            </dt>
            <dd>{stopWordEntity.key}</dd>
          </dl>
          <Button tag={Link} to="/entity/stop-word" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/stop-word/${stopWordEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ stopWord }: IRootState) => ({
  stopWordEntity: stopWord.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StopWordDetail);
