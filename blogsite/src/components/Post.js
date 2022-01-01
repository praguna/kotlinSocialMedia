import { Form, Button, TextArea, Input, Label } from 'semantic-ui-react'
import { getGravatarURL } from '../services/utils'
import { useState } from 'react'
import { savePost, deletepost } from '../services/post.service'
import { useNavigate } from "react-router-dom"

const Post = (props) => {
    const currentUser = props.currentUser
    const userPost = props.userPost
    const [title, setTitle] = useState(userPost.title)
    const [content, setContent] = useState(userPost.content)
    // const [showStatus, setShowStatus] = useState(false)
    const navigate = useNavigate()
    const handleSubmit = () => {
        if (title === "" || content === "") return
        const post = { title: title, content: content, "username": currentUser.user.username, "postId": userPost.postId ? userPost.postId : "0", userId: !userPost.userId ? "" : userPost.userId }
        console.log("submitted :", post)
        savePost(post).then((response) => {
            console.log("recieved post", response.data)
            navigate("/post/" + response.data.postId, { replace: true })
            window.location.reload();
        }).catch(err => {
            // setShowStatus(true)
            console.log("error creating post ::", err)
        })
    }



    const handleDelete = () => {
        const post = { "postId": userPost.postId }
        console.log("submitted :", post)
        deletepost(post).then((response) => {
            console.log("recieved delete")
            navigate("/", { replace: true })
        }).catch(err => {
            // setShowStatus(true)
            console.log("error deleting post ::", err)
        })
    }

    return (
        <div className='text-area-post'>
            <Form>
                <Label as='a' image>
                    <img src={getGravatarURL(userPost.username)} />
                    &nbsp;{userPost.username}&nbsp;
                    <Label.Detail>I love blogger</Label.Detail>
                </Label>
                <br /><br />
                <Input size='large' icon='home' placeholder='Title' value={title} onChange={(e) => { if (currentUser.user.username === userPost.username) setTitle(e.target.value) }} />
                <TextArea className='text-area-style' rows={12} wrap={"hard"} value={content} onChange={(e) => { if (currentUser.user.username === userPost.username) setContent(e.target.value) }} placeholder={"start typing..."} />
                <br /><br />
                <Button content='Post' labelPosition='left' icon='rocket' onClick={handleSubmit} color='green' hidden={currentUser.user.username !== userPost.username} />
                <Button content='Delete' onClick={handleDelete} labelPosition='left' icon='delete' color='red' hidden={currentUser.user.username !== userPost.username || !userPost.postId} />
            </Form>
        </div>
    )
}

export default Post

