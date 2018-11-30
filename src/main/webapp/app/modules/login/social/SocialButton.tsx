import axios from 'axios';
import React from 'react';
import { Button } from 'reactstrap';

export interface ISocialButtonProps {
  social: String;
  label: String;
}

function getSocialSetting(social) {
  switch (social) {
    case 'google':
      return 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email';
    case 'facebook':
      return 'public_profile,email';
    default:
      return 'Provider setting not defined';
  }
}

class SocialButton extends React.Component<ISocialButtonProps> {
  loginWithSocial = () => {
    // TODO: make an action of this in redux ??
    const data = {
      scope: getSocialSetting(this.props.social)
    };

    axios.post(`signin/${this.props.social}`, data).then(response => {
      window.console.log(response);
    });
  };

  render() {
    return (
      <Button color="primary" onClick={this.loginWithSocial}>
        {this.props.label}
      </Button>
    );
  }
}

export default SocialButton;
