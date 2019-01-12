import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/employee-account">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Employee Account
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/skill">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Skill
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/skill-level">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Skill Level
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/profile">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Profile
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/photo">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Photo
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/stop-word">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Stop Word
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/phone">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Phone
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/occupation">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Occupation
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/language-level">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Language Level
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/language">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Language
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/job-category">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Job Category
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/cv-file">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Cv File
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/contact">
      <FontAwesomeIcon icon="asterisk" />&nbsp;Contact
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/user-account">
      <FontAwesomeIcon icon="asterisk" />&nbsp;User Account
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
