const md5 = require( 'md5' );

function getGravatarURL( email ) {
  const address = String( email ).trim().toLowerCase();
  const hash = md5( address )
  return `https://www.gravatar.com/avatar/${ hash }?d=identicon`;
}

const HOST = "http://bloggerservice-dev.us-west-2.elasticbeanstalk.com/api"
// const HOST = "http://localhost:8080/api"
const API_DATA_URL = HOST+"/data/";
const API_AUTH_URL = HOST+"/auth/";

export {
    getGravatarURL,
    API_DATA_URL,
    API_AUTH_URL
}