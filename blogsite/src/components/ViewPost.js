import { React, useState, useEffect } from "react";
import AuthService from "../services/auth.service";
import { useNavigate } from "react-router-dom"
import ProfileMenu from "./ProfileMenu";
import CommentSection from "./CommentSection";
import Post from "./Post"
import HomeNav from "./HomeNav";
import {useParams} from "react-router-dom";
import {fetchPost} from '../services/post.service'
import CircleLoader from "./Loader";


const ViewPost = () => {

  const navigate = useNavigate()
  const data = useParams()
  const postId = data.postId

  const logOut = () => {
    AuthService.logout();
    navigate("/", { replace: true })
    window.location.reload();
  };

  const currentUser = AuthService.getCurrentUser();

  const [userPost, setUserPost] = useState({ 
    username: currentUser.user.username,
    description: "",
    title : "",
    content: "",
    userId: "",
  })


  useEffect(()=>{
    if(postId){
      fetchPost(postId).then((result)=>{
          let data = result.data
          setUserPost(data)
        }).catch(err=>{
            navigate("/", { replace: true })
            console.log("error ::",err)
        })
    }
  }, [])

  if (currentUser === null) {
    navigate("/", { replace: true })
    return (
      <div>
        <h5 className="text-center"><a href="/login">click here to login!!</a></h5>
      </div>
    )
  }


  const ui =  (
    <div>
      <HomeNav />
      <div id="outer-container">
        <ProfileMenu user={currentUser} logOut={logOut} />
        <main id="page-wrap">
            <Post currentUser={currentUser} userPost={userPost} />
            <CommentSection userPost={userPost} currentUser={currentUser} />
        </main>
      </div>
    </div>
  )

  if(!postId || (postId && userPost.postId)){
     return ui
  }

  return <CircleLoader/>

};

export default ViewPost;