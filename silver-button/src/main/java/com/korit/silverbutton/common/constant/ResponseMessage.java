package com.korit.silverbutton.common.constant;

public class ResponseMessage {

    // 성공 및 일반 메시지
    public static final String SUCCESS = "Success"; // 성공 시 반환 메시지
    public static final String VALIDATION_FAIL = "Validation failed."; // 유효성 검사 실패 시 반환 메시지
    public static final String DATABASE_ERROR = "Database error."; // 데이터베이스 에러 시 반환 메시지

    // 사용자 관련 메시지
    public static final String EXIST_USER = "User already exists."; // 사용자가 이미 존재할 때 반환 메시지
    public static final String NOT_EXIST_USER = "User does not exist."; // 사용자가 존재하지 않을 때 반환 메시지
    public static final String DUPLICATED_USER_ID = "Duplicated user ID."; // 사용자 ID 중복 시 반환 메시지

    // 로그인 및 인증 관련 메시지
    public static final String SIGN_IN_FAIL = "Sign in failed."; // 로그인 실패 시 반환 메시지
    public static final String AUTHENTICATION_FAIL = "Authentication failed."; // 인증 실패 시 반환 메시지
    public static final String NOT_MATCH_PASSWORD = "Password does not match."; // 비밀번호 불일치 시 반환 메시지
    public static final String NO_PERMISSION = "No permission."; // 권한이 없을 때 반환 메시지

    // 게시글 관련 메시지
    public static final String EXIST_POST = "Post already exists."; // 게시글이 이미 존재할 때 반환 메시지
    public static final String NO_COMMENTS_ON_POST = "No comments available on this post."; // 게시글에 댓글이 없을 때 반환 메시지
    public static final String NO_SEARCH_RESULTS = "No posts match the given search keyword."; // 검색 결과가 없을 때 반환 메시지
    public static final String POST_CREATION_SUCCESS = "Post successfully created."; // 게시글이 성공적으로 생성되었을 때 반환 메시지
    public static final String NOT_EXIST_POST = "Post does not exist."; // 게시글이 존재하지 않을 때 반환 메시지
    public static final String POST_UPDATE_SUCCESS = "Post successfully updated."; // 게시글이 성공적으로 수정되었을 때 반환 메시지
    public static final String POST_DELETION_SUCCESS = "Post successfully deleted."; // 게시글이 성공적으로 삭제되었을 때 반환 메시지
    public static final String POST_LIKE_SUCCESS = "Post liked successfully."; // 게시글에 좋아요를 성공적으로 추가했을 때 반환 메시지
    public static final String POST_UNLIKE_SUCCESS = "Post unliked successfully."; // 게시글 좋아요를 성공적으로 취소했을 때 반환 메시지
    public static final String POST_UPDATE_FAIL = "Failed to update the post."; // 게시글 수정 실패 시 반환 메시지
    public static final String POST_DELETION_FAIL = "Failed to delete the post."; // 게시글 삭제 실패 시 반환 메시지
    public static final String POST_NOT_ACCESSIBLE = "You do not have permission to access this post."; // 게시글에 접근 권한이 없을 때 반환 메시지
    public static final String POST_INACTIVE = "This post is inactive and cannot be modified."; // 비활성화된 게시글을 수정할 때 반환 메시지
    public static final String POST_LIKE_REMOVE_FAIL = "Failed to remove like from the post."; // 게시글 좋아요 취소 실패 시 반환 메시지
    public static final String POST_NOT_FOUND_FOR_USER = "No posts found for the given user."; // 특정 사용자의 게시글을 찾을 수 없을 때 반환 메시지
    public static final String INVALID_POST_ID = "Invalid post ID."; // 잘못된 게시글 ID가 요청될 때 반환 메시지
    public static final String INVALID_KEYWORD = "Invalid search keyword."; // 검색 키워드가 유효하지 않을 때 반환 메시지
    public static final String POST_ALREADY_LIKED = "You have already liked this post."; // 이미 좋아요를 누른 게시글에 다시 좋아요를 누를 때 반환 메시지

    // 댓글 관련 메시지
    public static final String EXIST_COMMENT = "Comment already exists."; // 댓글이 이미 존재할 때 반환 메시지
    public static final String NOT_EXIST_COMMENT = "Comment does not exist."; // 댓글이 존재하지 않을 때 반환 메시지

    // 기타 메시지
    public static final String TOKEN_CREATE_FAIL = "Token creation failed."; // 토큰 생성 실패 시 반환 메시지
    public static final String MESSAGE_SEND_FAIL = "Failed to send authentication number."; // 인증 번호 전송 실패 시 반환 메시지
}