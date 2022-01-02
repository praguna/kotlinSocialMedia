import { React, useState, useEffect } from "react";
import AuthService from "../services/auth.service";
import { useNavigate, useParams } from "react-router-dom"
import ProfileMenu from "./ProfileMenu";
import HomeNav from "./HomeNav";
import { Button, Header, Label } from 'semantic-ui-react'
import { myPosts, feedPosts, myPostsCount, feedPostsCount } from '../services/post.service'



const CardExampleLinkCard = (props) => {
  return (
    <div className="card " style={{ width: 40 + "rem", marginTop: 5 + "rem", marginRight: 2 + "rem", display: "inline-block" }}>
      <a href={"/post/" + props.userPost.postId} className="custom-card">
        <div className="card-body">
          <h4 className="card-title">{props.userPost.title || "Post Title"}&nbsp;&nbsp;&nbsp;&nbsp;<Label>{props.userPost.username || "Author"}</Label></h4>
          <h6 className="card-subtitle  text-muted">{props.userPost.createdTime}</h6>
          <br />
          <span className="card-text text-muted">{props.userPost.content ? props.userPost.content.slice(0, 50) : ""}...<b>(click to read more)</b></span>
        </div>
      </a>
    </div>
  )
}

const PostHeader = (props) => {
  return (
    <Header as='h2' dividing>
      {props.name} ({props.count})
    </Header>
  )
}

const Home = () => {

  const navigate = useNavigate()
  const data = useParams()
  const param = data.post !== "post" ? "mypost" : "post"
  const fetchFunc = data.post !== "post" ? myPosts : feedPosts
  const [cardInfo, setCardInfo] = useState([])
  const [count, setCount] = useState(0)
  const countFunc = data.post !== "post" ? myPostsCount : feedPostsCount
  const [err, setErr] = useState("")
  const [pageNum, setPageNum] = useState(0)
  const [loading, setLoading] = useState(false)
  const logOut = () => {
    AuthService.logout();
    navigate("/", { replace: true })
    window.location.reload();
  };

  const currentUser = AuthService.getCurrentUser();
  console.log(currentUser)


  useEffect(() => {
    console.log(pageNum)
    fetchFunc(currentUser.user.username, 0)
      .then((response) => {
        setErr("")
        setCardInfo(response.data)
        setPageNum(_page => _page + 1)
      }).catch(err => {
        console.log(err)
        setErr("error loading data")
      })
  }
    , [])

  useEffect(() => {
    countFunc(currentUser.user.username)
      .then((response) => {
        setErr("")
        setCount(response.data)
      }).catch(err => {
        console.log(err)
        setErr("error loading data")
      })
  })

  const handleLoadMore = ()=>{
    setLoading(true)
    fetchFunc(currentUser.user.username, pageNum)
      .then((response) => {
        setLoading(false)
        setErr("")
        setCardInfo(cardInfo.concat(response.data))
        setPageNum(_page => _page + 1)
      }).catch(err => {
        console.log(err)
        setLoading(false)
        setErr("error loading data")
      })
  }

  if (currentUser === null) {
    navigate("/", { replace: true })
    return (
      <div>
        <h5 className="text-center"><a href="/login">click here to login!!</a></h5>
      </div>
    )
  }


  return (
    <div>
      <HomeNav param={param} enable />
      <div id="outer-container">
        <ProfileMenu user={currentUser} logOut={logOut} />
        <main id="page-wrap">
          <div className="text-area-card">
            <PostHeader name={param !== "post" ? "Your Stories" : "Recent Stories"} count={count} />
            <div>
              {
               cardInfo.map((element, i)=>{
                 return <CardExampleLinkCard userPost={element} key = {i}/>
               })
             }
              {!err && count == 0 && (<h3 className="text-center red">Looks like there are no posts <span hidden={param === "post"}><a href="/post" >click here </a>to create</span></h3>)}
              {err && (
                <div className="form-group">
                  <div
                    className="alert alert-danger"
                    role="alert">
                    {err}
                  </div>
                </div>
              )}
            </div>
            {!err && loading && (<h3>loading...</h3>)  }
            {(!err && count && <Button primary hidden = {count - cardInfo.length <= 0 } onClick={handleLoadMore}>Load More</Button>)}
          </div>
        </main>
      </div>
    </div>
  )

};

export default Home;
