import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import {
  openFile,
  byteSize,
  ICrudGetAllAction,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './profile.reducer';
import { IProfile } from 'app/shared/model/profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IProfileState = IPaginationBaseState;

export class Profile extends React.Component<IProfileProps, IProfileState> {
  state: IProfileState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { profileList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="profile-heading">
          Profiles
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Profile
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('salaryAmount')}>
                  Salary Amount <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('city')}>
                  City <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('description')}>
                  Description <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('experience')}>
                  Experience <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('jobExpectations')}>
                  Job Expectations <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('achievements')}>
                  Achievements <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('positionName')}>
                  Position Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('y')}>
                  Y <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {profileList.map((profile, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${profile.id}`} color="link" size="sm">
                      {profile.id}
                    </Button>
                  </td>
                  <td>{profile.salaryAmount}</td>
                  <td>{profile.city}</td>
                  <td>{profile.description}</td>
                  <td>{profile.experience}</td>
                  <td>{profile.jobExpectations}</td>
                  <td>{profile.achievements}</td>
                  <td>{profile.positionName}</td>
                  <td>
                    {profile.y ? (
                      <div>
                        <a onClick={openFile(profile.yContentType, profile.y)}>
                          <img src={`data:${profile.yContentType};base64,${profile.y}`} style={{ maxHeight: '30px' }} />
                          &nbsp;
                        </a>
                        <span>
                          {profile.yContentType}, {byteSize(profile.y)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${profile.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profile.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profile.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ profile }: IRootState) => ({
  profileList: profile.entities,
  totalItems: profile.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Profile);
