import { push as Menu } from 'react-burger-menu'
import { getGravatarURL } from '../services/utils'

function camelize(str) {
  return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index) {
    return index === 0 ? word.toLowerCase() : word.toUpperCase();
  }).replace(/\s+/g, '');
}

const ProfileMenu = (props)=>{
  const currentUser = props.user.user
    return (
      <Menu pageWrapId={"page-wrap"} noTransition outerContainerId={"outer-container"} isOpen={false} itemListElement="div" noOverlay disableAutoFocus>
        <h3 id="home" className="menu-item">Welcome To Blogger !!!</h3>
        <br/><br/><br/>
        <img className="menu-item" style={{marginLeft : 3 + "rem"}}  src={getGravatarURL(currentUser.username)} />
        <p id="user" className='menu-item' style={{marginLeft : 3 + "rem"}} >{currentUser.username.toUpperCase()}</p>
        <br/><br/><br/>
        <a className="menu-item" href="/post">Create Post</a>
        <a className="menu-item" onClick={props.logOut}>LogOut</a>
      </Menu>
    )

}

export default ProfileMenu