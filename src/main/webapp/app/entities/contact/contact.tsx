import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, getSortState, IPaginationBaseState, getPaginationItemsNumber, JhiPagination } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IContactProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IContactState = IPaginationBaseState;

export class Contact extends React.Component<IContactProps, IContactState> {
  state: IContactState = {
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
    const { contactList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="contact-heading">
          Contacts
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp; Create new Contact
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('name')}>
                  Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('lastName')}>
                  Last Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('skype')}>
                  Skype <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('github')}>
                  Github <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('telegram')}>
                  Telegram <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('linkedin')}>
                  Linkedin <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('email')}>
                  Email <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contactList.map((contact, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${contact.id}`} color="link" size="sm">
                      {contact.id}
                    </Button>
                  </td>
                  <td>{contact.name}</td>
                  <td>{contact.lastName}</td>
                  <td>{contact.skype}</td>
                  <td>{contact.github}</td>
                  <td>{contact.telegram}</td>
                  <td>{contact.linkedin}</td>
                  <td>{contact.email}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${contact.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${contact.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${contact.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ contact }: IRootState) => ({
  contactList: contact.entities,
  totalItems: contact.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Contact);
