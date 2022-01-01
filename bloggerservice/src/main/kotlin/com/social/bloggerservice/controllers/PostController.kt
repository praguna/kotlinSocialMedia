package com.social.bloggerservice.controllers

import com.social.bloggerservice.models.*
import com.social.bloggerservice.services.UserPostService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/data")
class PostController(
    private val userPostService: UserPostService
) {

    private val log  = LoggerFactory.getLogger(PostController::class.java)

    @DeleteMapping("/comments/delete/{commentId}")
    suspend fun deleteComments(@PathVariable commentId : String) : ResponseEntity<Boolean>{
        log.info("delete comment :: $commentId")
        return ResponseEntity.ok(userPostService.deleteComment(commentId))
    }

    @DeleteMapping("/post/delete/{postId}")
    suspend fun deletePost(@PathVariable postId : String) : ResponseEntity<Boolean>{
        log.info("delete post ::  $postId")
        return ResponseEntity.ok(userPostService.deletePost(postId))
    }

    @PostMapping("/post/update")
    suspend fun updatePost(@Validated @RequestBody body : SavePost) : ResponseEntity<PostUpdateResponse> {
        log.info("request body :: $body" )
        return ResponseEntity.ok(userPostService.savePost(body))
    }

    @PostMapping("/comments/update")
    suspend fun updateComments(@Valid @RequestBody body : SaveComment):ResponseEntity<CommentResponse>{
       log.info("comment body :: $body")
       val response = userPostService.saveComment(body)
       return ResponseEntity.ok(response)
    }

    @GetMapping("/comments/{parentId}")
    suspend fun fetchComments(@PathVariable parentId : String) : List<CommentResponse>{
        log.info("comment parentId :: $parentId")
        return userPostService.fetchComments(parentId)
    }

    @GetMapping("/post/{postId}")
    suspend fun fetchPost(@PathVariable postId : String) : ResponseEntity<PostResponse>{
        log.info("post postId :: $postId")
        return ResponseEntity.ok(userPostService.fetchPost(postId))
    }

    @GetMapping("post/myposts/{username}/{pageNum}")
    suspend fun fetchMyPosts(@PathVariable username: String, @PathVariable pageNum: Int): ResponseEntity<List<PostResponse>>{
        log.info("myposts :: $username")
        return ResponseEntity.ok(userPostService.paginatedFetchPostUser(username, pageNum))
    }

    @GetMapping("post/myposts/{username}/count")
    suspend fun fetchMyPostsCount(@PathVariable username: String): ResponseEntity<Long>{
        log.info("myposts :: $username")
        return ResponseEntity.ok(userPostService.paginatedFetchPostUserCount(username))
    }

    @GetMapping("post/feed/{username}/count")
    suspend fun fetchFeedCount(@PathVariable username: String): ResponseEntity<Long>{
        log.info("myposts :: $username")
        return ResponseEntity.ok(userPostService.paginatedFetchPostUserCountNot(username))
    }


    @GetMapping("post/feed/{username}/{pageNum}")
    suspend fun fetchFeedPosts(@PathVariable username: String,  @PathVariable pageNum: Int): ResponseEntity<List<PostResponse>>{
        log.info("myposts :: $username")
        return ResponseEntity.ok(userPostService.paginatedFetchPostNonUser(username, pageNum))
    }
}