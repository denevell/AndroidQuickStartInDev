package org.denevell.AndroidProject.services;

import org.denevell.AndroidProject.services.MessageBusService.GetResult;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public class RecentPostsService {

    private MessageBusService<RecentPosts, RecentPostsServiceInterface> mService;

    public RecentPostsService() {
        mService = new MessageBusService<>();
    }

    public void fetch(final int start, final int num) {
        mService.fetch(
            "https://android-manchester.co.uk/api/rest1",
            RecentPostsServiceInterface.class,
            new RecentPostsError(),
            new GetResult<RecentPosts, RecentPostsServiceInterface>() {
                @Override public RecentPosts getResult(RecentPostsServiceInterface service) {
                    return service.go(start, num);
                }
            });
    }

    public static interface RecentPostsServiceInterface {
        @GET("/post/{start}/{num}")
        RecentPosts go(@Path("start") int start, @Path("num") int num);
    }

    public static class RecentPosts {

        private List<Post> posts;

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        private class Post {
            private String subject;

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }
        }
    }

    public static class RecentPostsError extends ErrorResponse {}
}
