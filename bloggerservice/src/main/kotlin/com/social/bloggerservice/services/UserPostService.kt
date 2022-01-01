package com.social.bloggerservice.services

import com.social.bloggerservice.models.*
import com.social.bloggerservice.repositories.CommentRepository
import com.social.bloggerservice.repositories.PostRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class UserPostService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {

    suspend fun deletePost(id : String): Boolean{
        return Flux.merge(commentRepository.deleteAllByParentId(id).thenReturn(true) , postRepository.deleteById(ObjectId(id))).then(
            Mono.just(true)).awaitSingle()
    }

    suspend fun deleteComment(id : String): Boolean{
        return commentRepository.deleteById(ObjectId(id)).thenReturn(true).awaitSingle()
    }

    suspend fun savePost(post : SavePost) : PostUpdateResponse{
        val id  =  if(post.postId != "0") ObjectId(post.postId) else ObjectId.get()
        return postRepository.save(Post(
            id = id,
            username = post.username,
            content = post.content,
            title = post.title,
        )).map { PostUpdateResponse(id.toHexString()) }.awaitSingle()
    }

    suspend fun saveComment(body: SaveComment) : CommentResponse{
        val comment =  commentRepository.save(Comment(
            username = body.username,
            content = body.content,
            parentId = body.parentId
        )).awaitSingle()

        return commentResponse(comment)
    }

    suspend fun fetchComments(id : String): List<CommentResponse>{
        return commentRepository.findAllByParentIdOrderByCreatedTimeDesc(id).map { commentResponse(it) }.collectList().awaitSingle()
    }

    private fun commentResponse(comment: Comment) =
        CommentResponse(
            commentId = comment.id.toHexString(),
            username = comment.username, content = comment.content, createdTime = comment.createdTime.convertToWords(),
            parentId = comment.parentId
        )

    fun LocalDateTime.convertToWords() : String {
        val now = this.atZone(ZoneId.of("Asia/Kolkata"))
        val formatTime = SimpleDateFormat("hh.mm aa")
        val A = Date.from(now.toInstant()).toString().split(' ').subList(0, 3).joinToString(" ")
        val B = formatTime.format(Date.from(now.toInstant())).toString()
        val C = now.year
        return "$A $C $B"
    }

    suspend fun fetchPost(postId: String) : PostResponse{
        val post = postRepository.findById(ObjectId(postId)).awaitSingle()
        return postResponse(postId, post)
    }

    private fun postResponse(
        postId: String,
        post: Post
    ) = PostResponse(postId, post.username, post.title, post.content, post.createdTime.convertToWords())

    suspend fun paginatedFetchPostUser(username : String, pageNum : Int): List<PostResponse>{
        return postRepository.findByUsernameOrderByCreatedTimeDesc(username, PageRequest.of(pageNum, 5)).map { postResponse(it.id.toHexString(), it) }.collectList().awaitSingle()
    }

    suspend fun paginatedFetchPostUserCount(username : String): Long{
        return postRepository.countByUsername(username).awaitSingle()
    }

    suspend fun paginatedFetchPostUserCountNot(username : String): Long{
        return postRepository.countByUsernameNot(username).awaitSingle()
    }

    suspend fun paginatedFetchPostNonUser(username : String, pageNum: Int): List<PostResponse>{
        return postRepository.findByUsernameNotOrderByCreatedTimeDesc(username, PageRequest.of(pageNum, 5)).map { postResponse(it.id.toHexString(), it) }.collectList().awaitSingle()
    }
}