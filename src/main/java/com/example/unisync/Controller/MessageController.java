package com.example.unisync.Controller;

import com.example.unisync.Config.Auth.UserInfoDetails;
import com.example.unisync.DTO.MessageDTO;
import com.example.unisync.DTO.ReplyDTO;
import com.example.unisync.Exception.UnauthorizedException;
import com.example.unisync.Mapper.MessageMapper;
import com.example.unisync.Model.Message;
import com.example.unisync.Service.MessageLikeService;
import com.example.unisync.Service.MessageService;
import com.example.unisync.Service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.example.unisync.Config.Constants.*;

@RestController
@RequestMapping("/api/message")
@Api(tags = "Message Controller", description = "Operations related to messages")
public class MessageController extends BaseController{
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final MessageLikeService messageLikeService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, MessageMapper messageMapper, MessageLikeService messageLikeService, UserService userService) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
        this.messageLikeService = messageLikeService;
        this.userService = userService;
    }

    @PostMapping("/post")
    @ApiOperation(value = "Write a new message", notes = "Post a new message as a user in a course you are enrolled in")
    public ResponseEntity<MessageDTO> postMessage(@RequestBody MessageDTO messageDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());
            messageDTO.setUserId(currentUser.get().getId());

            Message postedMessage = messageService.postMessage(messageDTO);
            return new ResponseEntity<>(messageMapper.map(postedMessage), HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/like/{messageId}")
    @ApiOperation(value = "Like a message", notes = "Like a message as a user in a course channel you are enrolled in")
    public ResponseEntity<String> likeMessage(@PathVariable Long messageId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());

            messageLikeService.likeMessage(currentUser.get().getId(), messageId);
            return new ResponseEntity<>(MESSAGE_LIKED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_LIKING_MESSAGE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unlike/{messageId}")
    @ApiOperation(value = "Unlike a message", notes = "Unlike one of your liked messages as a user in a course channel you are enrolled in")
    public ResponseEntity<String> unlikeMessage(@PathVariable Long messageId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());

            messageLikeService.unlikeMessage(currentUser.get().getId(), messageId);
            return new ResponseEntity<>(MESSAGE_UNLIKED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_UNLIKING_MESSAGE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reply")
    @ApiOperation(value = "Reply to a message", notes = "Reply to a message as a user in a course channel you are enrolled in")
    public ResponseEntity<String> createReply(@RequestBody ReplyDTO replyDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());
            replyDTO.setUserId(currentUser.get().getId());

            messageService.postReply(replyDTO);
            return new ResponseEntity<>(REPLY_CREATED_SUCCESSFULLY, HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_CREATING_REPLY + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
