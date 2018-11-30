import Cookie from 'js-cookie';
import React from 'react';
import { Button } from 'reactstrap';

export interface ISocialButtonOldFashionedProps {
  social: string;
  label: string;
}

function getSocialUrl(social) {
  return `signin/${social}`;
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

function getCSRF() {
  return Cookie.get('XSRF-TOKEN');
}

class SocialButtonOldFashioned extends React.Component<ISocialButtonOldFashionedProps> {
  render() {
    const { social, label } = this.props;
    const csrf = getCSRF();
    const socialSetting = getSocialSetting(social);

    return (
      <form action={getSocialUrl(social)} method="POST">
        <Button color="primary" type="submit">
          {label}
        </Button>
        <input name="scope" type="hidden" value={socialSetting} />
        <input name="_csrf" type="hidden" value={csrf} />
      </form>
    );
  }
}

export default SocialButtonOldFashioned;
