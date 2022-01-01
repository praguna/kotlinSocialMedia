import React, { useState, useEffect } from 'react'
import { Button, Comment, Form, Header } from 'semantic-ui-react'
import { deleteComment, fetchComments } from '../services/post.service'
import { getGravatarURL } from '../services/utils'
import { postComment } from '../services/post.service'

const CommentSection = (props) => {

    const [comments, setComments] = useState([])
    const currentUser = props.currentUser
    const [userComment, setUserComment] = useState("")
    

    useEffect(()=>{
        if(!props.userPost.postId) return
        fetchComments(props.userPost.postId)
        .then((response)=>{
            setComments(response.data)
        }).catch(err=>{
            console.log("error :: ", err)
        })
    }, [])

    const handleDelete = (comment, i) => {
        return () => {
            deleteComment(comment.commentId).then(
                () => {
                    setComments(comments.filter((e, index) => index != i))
                }
            ).catch((err) => {
                console.log("could not remove :", err)
            })
        }
    }

    const handleSumit = (e) => {
        if(userComment === "") return
        const commentPost = { "parentId": props.userPost.postId, "username": currentUser.user.username, "content": userComment }
        console.log("posted comment :", commentPost)
        postComment(commentPost).then((response) => {
            console.log("recieved post")
            setUserComment("")
            setComments([response.data].concat(comments))

        }).catch(err => {
            console.log("error", err)
        })
    }

   

    // if it is a create page -- so no post-id
    return !props.userPost.postId? (null) : (

        <Comment.Group>
            <Header as='h4' dividing>
                Comments ({comments.length})
            </Header>


            <Form reply onSubmit={handleSumit}>
                <Form.TextArea rows={2} value={userComment} onChange={(e) => { setUserComment(e.target.value) }} />
                <Button content='Comment' labelPosition='left' icon='edit' primary />
            </Form>

            {
                comments.map((element, i) => {
                    return <Comment key={i}>
                        <Comment.Avatar as='a' src={getGravatarURL(element.username)} />
                        <Comment.Content>
                            <Comment.Author as='a'>{element.username}</Comment.Author>
                            <Comment.Metadata>
                                <span>{element.createdTime}</span>
                            </Comment.Metadata>
                            <Comment.Text><p>{element.content}</p></Comment.Text>
                            {
                                element.username === currentUser.user.username ?
                                    <Comment.Actions>
                                        <a onClick={handleDelete(element, i)}>Delete</a>
                                    </Comment.Actions>
                                    : null

                            }
                        </Comment.Content>
                    </Comment>
                })
            }


        </Comment.Group>
    )
}

export default CommentSection
