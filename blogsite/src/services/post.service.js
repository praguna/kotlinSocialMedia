import axios from "axios";
import {API_DATA_URL as API_URL} from './utils'

// const comments_1 = [
//     {commentId : "a1",   username: "praguna",  content : "How artistic!",  parentId : "p1", createdTime : "Today at 5:42PM"},
//     {commentId : "a2",   username: "Eliot" ,content : "This has been very useful for my research. Thanks as well!",  parentId : "p1", createdTime : "Today at 5:43PM"},
//     {commentId : "a3",   username: "Joe", content : "Elliot you are always so right :)",  parentId : "p1", createdTime : "Today at 6:30M"},
   
// ]

// const userpost = {
//     postId : "61d05434215ceb213e44e1d4",
//     username: "Joe",
//     title : "My title",
//     content: "The hours, minutes and seconds stand as visible reminders that your effort put them all there.Preserve until your next run, when the watch lets you see how Impermanent your efforts are.",
//     createdTime: "23rd Oct. 5:30pm"
// }

// const userpost = {
//     postId : "",
//     username: "",
//     description: "",
//     title : "",
//     content: "",
//     createdTime: ""
// }

// const cardInfo = [{
//         postId : "61d05434215ceb213e44e1d4",  
//         username: "Joe", 
//         title : "My title", 
//         content: "The hours, minutes and seconds stand as visible reminders that your effort put them all there.Preserve until your next run, when the watch lets you see how Impermanent your efforts are.", 
//         createdTime: "23rd Oct. 5:30pm"
// }]



const fetchComments = (parentId)=>{
    return axios.get(API_URL + "comments/" + parentId)
} 

const fetchPost = (postId)=>{
    return axios.get(API_URL + "post/" + postId)
}

const deleteComment = (commentId)=>{
    return axios.delete(API_URL + "comments/" + "delete/" + commentId)
}

const savePost = (postData) => {
    return axios.post(API_URL + "post/" + "update/", postData)
}

const postComment = (commentData) => {
    return axios.post(API_URL + "comments/" + "update/", commentData)
}

const deletepost = (data) => {
    return axios.delete(API_URL + "post/" + "delete/" + data.postId)
}

const feedPosts = (username, pageNum)=>{
    return axios.get(API_URL + "post/feed/" + username+ "/" + pageNum)
}

const myPosts = (username, pageNum)=>{
    return axios.get(API_URL + "post/myposts/" + username+"/" +pageNum)
}

const myPostsCount = (username) => {
    return axios.get(API_URL + "post/myposts/" + username+"/count")
}

const feedPostsCount = (username) => {
    return axios.get(API_URL + "post/feed/" + username+"/count")
}


export  {
    fetchComments,
    fetchPost,
    deleteComment,
    savePost,
    postComment,
    deletepost,
    myPosts,
    feedPosts,
    myPostsCount,
    feedPostsCount
}