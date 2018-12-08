import SocialButtonOldFashioned from './SocialButtonOldFashioned';
import React from 'react';
import { Button } from 'reactstrap';

// TODO: accept params from router
class SocialRegister extends React.Component {
  render() {
    return (
      <div>
        <div className="row">
          <div className="col-md-8 col-md-offset-2">
            <h1>Registration with</h1>
            <SocialButtonOldFashioned label="Sign in with Facebook" social="facebook" />
          </div>
        </div>
      </div>
    );
  }
}

export default SocialRegister;
