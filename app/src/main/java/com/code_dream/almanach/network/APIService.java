package com.code_dream.almanach.network;

import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Message;
import com.code_dream.almanach.models.Note;
import com.code_dream.almanach.models.Notification;
import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.SearchResult;
import com.code_dream.almanach.models.geocoding.LocationsResponse;
import com.code_dream.almanach.network.requests.CalendarEventsForSubjectRequest;
import com.code_dream.almanach.network.requests.CreateCalendarEventRequest;
import com.code_dream.almanach.network.requests.CreateCommentRequest;
import com.code_dream.almanach.network.requests.CreatePostRequest;
import com.code_dream.almanach.network.requests.DeleteCalendarEventRequest;
import com.code_dream.almanach.network.requests.DeleteNoteRequest;
import com.code_dream.almanach.network.requests.DeletePostRequest;
import com.code_dream.almanach.network.requests.DeleteSubjectRequest;
import com.code_dream.almanach.network.requests.EditCalendarEventRequest;
import com.code_dream.almanach.network.requests.EditNoteRequest;
import com.code_dream.almanach.network.requests.EditPostRequest;
import com.code_dream.almanach.network.requests.FirebaseTokenUpdateRequest;
import com.code_dream.almanach.network.requests.LoadAllMessagesRequest;
import com.code_dream.almanach.network.requests.NewDobRequest;
import com.code_dream.almanach.network.requests.NewNameRequest;
import com.code_dream.almanach.network.requests.NewNoteRequest;
import com.code_dream.almanach.network.requests.NewSubjectRequest;
import com.code_dream.almanach.network.requests.PersonRequest;
import com.code_dream.almanach.network.requests.ReportCommentRequest;
import com.code_dream.almanach.network.requests.SchoolPostCommentsRequest;
import com.code_dream.almanach.network.requests.SchoolPostsForPersonRequest;
import com.code_dream.almanach.network.requests.SchoolPostsRequest;
import com.code_dream.almanach.network.requests.PostReactionRequest;
import com.code_dream.almanach.network.requests.ReportPostRequest;
import com.code_dream.almanach.network.requests.SearchRequest;
import com.code_dream.almanach.network.requests.SetSchoolRequest;
import com.code_dream.almanach.models.CalendarEvent;
import com.code_dream.almanach.network.responses.LoginResponse;
import com.code_dream.almanach.network.responses.LogoutResponse;
import com.code_dream.almanach.models.School;
import com.code_dream.almanach.models.SchoolPost;
import com.code_dream.almanach.network.responses.RegisterResponse;
import com.code_dream.almanach.network.requests.LoginRequest;
import com.code_dream.almanach.network.requests.RegisterRequest;
import com.code_dream.almanach.models.TimetableItem;
import com.code_dream.almanach.network.responses.TokenValidationResponse;
import com.code_dream.almanach.network.responses.UpdatePreferencesResponse;
import com.code_dream.almanach.post_details.SchoolPostComment;
import com.code_dream.almanach.models.SubjectListItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Qwerasdzxc on 2/13/17.
 */

public interface APIService {

    @POST("auth/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @POST("auth/register/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("auth/logout/")
    Call<LogoutResponse> logoutUser(@Body String emptyBody);

    @POST("auth/update-firebase-token/")
    Call<ResponseBody> updateFirebaseToken(@Body FirebaseTokenUpdateRequest request);

    @POST("auth/token-validation/")
    Call<TokenValidationResponse> verifyTokenValidation(@Body String emptyBody);

    @POST("social/search/")
    Call<SearchResult> search(@Body SearchRequest request);

    @POST("social/search-schools/")
    Call<List<School>> searchSchool(@Body SearchRequest request);

    @POST("social/get-person/")
    Call<Person> getPerson(@Body PersonRequest request);

    @POST("chat/get-all-messages/")
    Call<List<Message>> loadAllMessages(@Body LoadAllMessagesRequest request);

    @POST("chat/create-chat-room/")
    Call<ChatRoom> createChatRoom(@Body PersonRequest request);

    @POST("chat/get-chat-rooms/")
    Call<List<ChatRoom>> getChatRooms(@Body String emptyBody);

    @POST("social/delete-friend-request/")
    Call<ResponseBody> deleteFriend(@Body PersonRequest request);

    @POST("social/send-friend-request/")
    Call<ResponseBody> sendFriendRequest(@Body PersonRequest request);

    @POST("social/accept-friend-request/")
    Call<ResponseBody> acceptFriendRequest(@Body PersonRequest request);

    @POST("social/get-school-posts/")
    Call<List<SchoolPost>> getSchoolPosts(@Body SchoolPostsRequest request);

    @POST("social/get-school-posts-for-person/")
    Call<List<SchoolPost>> getSchoolPostsForPerson(@Body SchoolPostsForPersonRequest request);

    @POST("social/get-school-post-comments/")
    Call<List<SchoolPostComment>> getSchoolPostComments(@Body SchoolPostCommentsRequest request);

    @POST("social/new-post/")
    Call<SchoolPost> createPost(@Body CreatePostRequest request);

    @Multipart
    @POST("social/new-post/")
    Call<SchoolPost> createPost(@Part("post_content_part") CreatePostRequest request, @Part MultipartBody.Part imageFile);

    @POST("social/edit-post/")
    Call<SchoolPost> editPost(@Body EditPostRequest request);

    @POST("social/new-comment/")
    Call<SchoolPostComment> createComment(@Body CreateCommentRequest request);

    @POST("social/edit-comment/")
    Call<SchoolPostComment> editComment(@Body EditPostRequest request);

    @POST("social/upvote-post/")
    Call<ResponseBody> upvotePost(@Body PostReactionRequest request);

    @POST("social/downvote-post/")
    Call<ResponseBody> downvotePost(@Body PostReactionRequest request);

    @POST("social/upvote-comment/")
    Call<ResponseBody> upvoteComment(@Body PostReactionRequest request);

    @POST("social/downvote-comment/")
    Call<ResponseBody> downvoteComment(@Body PostReactionRequest request);

    @POST("social/report-post/")
    Call<ResponseBody> reportPost(@Body ReportPostRequest request);

    @POST("social/delete-post/")
    Call<ResponseBody> deletePost(@Body DeletePostRequest request);

    @POST("social/report-comment/")
    Call<ResponseBody> reportComment(@Body ReportCommentRequest request);

    @POST("social/delete-comment/")
    Call<ResponseBody> deleteComment(@Body DeletePostRequest request);

    @POST("social/set-school/")
    Call<ResponseBody> setSchool(@Body SetSchoolRequest request);

    @POST("social/update-preferences/")
    Call<UpdatePreferencesResponse> updatePreferences(@Body String emptyBody);

    @POST("social/name-change/")
    Call<ResponseBody> changeName(@Body NewNameRequest request);

    @POST("social/dob-change/")
    Call<ResponseBody> changeDob(@Body NewDobRequest request);

    @POST("social/delete-user/")
    Call<ResponseBody> deleteUser(@Body String emptyBody);

    @POST("social/get-notifications/")
    Call<List<Notification>> getNotifications(@Body String emptyBody);

    @POST("calendar/get-calendar-events/")
    Call<List<CalendarEvent>> getCalendarEvents(@Body String emptyBody);

    @POST("calendar/add-calendar-event/")
    Call<CalendarEvent> addCalendarEvent(@Body CreateCalendarEventRequest request);

    @POST("calendar/edit-calendar-event/")
    Call<ResponseBody> editCalendarEvent(@Body EditCalendarEventRequest request);

    @POST("calendar/delete-calendar-event/")
    Call<ResponseBody> deleteCalendarEvent(@Body DeleteCalendarEventRequest request);

    @POST("subjects/upload-timetable/")
    Call<ResponseBody> uploadTimetable(@Body ArrayList<TimetableItem> timetableItems);

    @POST("subjects/load-timetable/")
    Call<List<TimetableItem>> loadTimetable(@Body String emptyBody);

    @POST("subjects/load-timetable-for-today/")
    Call<List<TimetableItem>> loadTimetableForToday(@Body String emptyBody);

    @POST("subjects/get-calendar-events-for-subject/")
    Call<List<CalendarEvent>> getCalendarEventsForSubject(@Body CalendarEventsForSubjectRequest request);

    @POST("subjects/get-all-subjects/")
    Call<List<SubjectListItem>> getAllSubjects(@Body String emptyBody);

    @POST("subjects/get-all-subject-names/")
    Call<List<SubjectListItem>> getAllSubjectNames(@Body String emptyBody);

    @POST("subjects/delete-subject/")
    Call<ResponseBody> deleteSubject(@Body DeleteSubjectRequest request);

    @POST("subjects/add-subject/")
    Call<ResponseBody> addSubject(@Body NewSubjectRequest request);

    @POST("subjects/get-all-notes/")
    Call<List<Note>> getAllNotes(@Body String emptyBody);

    @POST("subjects/add-note/")
    Call<Note> addNote(@Body NewNoteRequest request);

    @POST("subjects/delete-note/")
    Call<ResponseBody> deleteNote(@Body DeleteNoteRequest request);

    @POST("subjects/edit-note/")
    Call<Note> editNote(@Body EditNoteRequest request);

    @GET
    Call<LocationsResponse> getFromLocationName(@Url String url);
}
