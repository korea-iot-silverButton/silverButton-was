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
    public static final String DUPLICATED_USER_EMAIL = "Duplicated user EMAIL."; // 사용자 email 중복 시 반환 메시지

    // 로그인 및 인증 관련 메시지
    public static final String SIGN_IN_FAIL = "Sign in failed."; // 로그인 실패 시 반환 메시지
    public static final String AUTHENTICATION_FAIL = "Authentication failed."; // 인증 실패 시 반환 메시지
    public static final String NOT_MATCH_PASSWORD = "Password does not match."; // 비밀번호 불일치 시 반환 메시지
    public static final String NO_PERMISSION = "No permission."; // 권한이 없을 때 반환 메시지

    // 게시글 관련 메시지 - 황상기
    public static final String NO_SEARCH_RESULTS = "No posts match the given search keyword."; // 검색 결과가 없을 때 반환 메시지
    public static final String SEARCH_RESULTS_FOUND = "Search results found for the given keyword."; // 검색 결과가 있을 때 반환 메시지
    public static final String POST_CREATION_SUCCESS = "Post successfully created."; // 게시글이 성공적으로 생성되었을 때 반환 메시지
    public static final String POST_CREATION_FAIL = "게시글 생성에 실패했습니다.";
    public static final String NOT_EXIST_POST = "Post does not exist."; // 게시글이 존재하지 않을 때 반환 메시지
    public static final String POST_UPDATE_SUCCESS = "Post successfully updated."; // 게시글이 성공적으로 수정되었을 때 반환 메시지
    public static final String POST_DELETION_SUCCESS = "Post successfully deleted."; // 게시글이 성공적으로 삭제되었을 때 반환 메시지
    public static final String POST_LIKE_SUCCESS = "Post liked successfully."; // 게시글에 좋아요를 성공적으로 추가했을 때 반환 메시지
    public static final String POST_UNLIKE_SUCCESS = "Post unliked successfully."; // 게시글 좋아요를 성공적으로 취소했을 때 반환 메시지
    public static final String POST_UPDATE_FAIL = "Failed to update the post."; // 게시글 수정 실패 시 반환 메시지
    public static final String POST_DELETION_FAIL = "Failed to delete the post."; // 게시글 삭제 실패 시 반환 메시지
    public static final String POST_NOT_FOUND_FOR_USER = "No posts found for the given user."; // 특정 사용자의 게시글을 찾을 수 없을 때 반환 메시지
    public static final String POST_FOUND_FOR_USER = "Posts found for the given user."; // 특정 사용자의 게시글이 있을 때 반환 메시지
    public static final String INVALID_POST_ID = "Invalid post ID."; // 잘못된 게시글 ID가 요청될 때 반환 메시지
    public static final String INVALID_KEYWORD = "Invalid search keyword."; // 검색 키워드가 유효하지 않을 때 반환 메시지
    public static final String POST_DETAIL_NOT_FOUND = "Post not found."; // 게시글 단건 조회 실패 메시지
    public static final String POST_DETAIL_FOUND = "Post found successfully."; // 게시글 단건 조회 성공 메시지

    // 댓글 관련 메시지 - 황상기
    public static final String POST_COMMENT_CREATION_SUCCESS = "Comment created successfully."; // 댓글 생성 성공
    public static final String POST_COMMENT_CREATION_FAILED = "Failed to create comment."; // 댓글 생성 실패
    public static final String NOT_EXIST_COMMENT = "Comment does not exist."; // 댓글이 존재하지 않을 때 반환 메시지
    public static final String COMMENT_EXISTS = "Comment exists."; // 댓글이 존재할 때 반환 메시지
    public static final String COMMENT_DELETE_SUCCESS = "Comment deleted successfully."; // 댓글 삭제 성공 메시지
    public static final String COMMENT_DELETE_FAILED = "Comment deletion failed. Comment does not exist."; // 댓글 삭제 실패 메시지


    // 기타 메시지
    public static final String TOKEN_CREATE_FAIL = "Token creation failed."; // 토큰 생성 실패 시 반환 메시지
    public static final String MESSAGE_SEND_FAIL = "Failed to send authentication number."; // 인증 번호 전송 실패 시 반환 메시지
    public static final String LOGOUT_FAILED = "Logout Failed";
    public static final String UNAUTHORIZED = "NOT Dependent error";

    // 프로필 이미지 관련 메시지
    public static final String PROFILE_IMG_UPLOAD_SUCCESS = "Profile image uploaded successfully."; // 프로필 이미지 업로드 성공
    public static final String PROFILE_IMG_UPLOAD_FAIL = "Failed to upload profile image."; // 프로필 이미지 업로드 실패
    public static final String PROFILE_IMG_DELETE_SUCCESS = "Profile image deleted successfully."; // 프로필 이미지 삭제 성공
    public static final String PROFILE_IMG_DELETE_FAIL = "Failed to delete profile image."; // 프로필 이미지 삭제 실패
    public static final String PROFILE_IMG_NOT_FOUND = "Profile image not found."; // 프로필 이미지 없음
    public static final String PROFILE_IMG_UPDATE_SUCCESS = "Profile image updated successfully."; // 프로필 이미지 업데이트 성공
    public static final String PROFILE_IMG_UPDATE_FAIL = "Failed to update profile image."; // 프로필 이미지 업데이트 실패

}