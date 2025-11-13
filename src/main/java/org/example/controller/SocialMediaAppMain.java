package org.example.controller;

import org.example.service.FollowerService;
import org.example.service.FriendRequestService;
import org.example.service.PostService;
import org.example.service.UserService;

import java.util.Scanner;

public class SocialMediaAppMain {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);
    private static final PostService postService = new PostService();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        FollowerService followService = new FollowerService();
        FriendRequestService friendService = new FriendRequestService();

        String loggedUser = null;
        int choice;

        log.info(" SOCIAL MEDIA APP ");

        while (true) {
            if (loggedUser == null) {

                log.info("1. Register");
                log.info("2. Login");
                log.info("3. Forgot Password");
                log.info("4. Exit");

                log.info("Choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        log.info("Username: ");
                        String u = sc.nextLine();
                        log.info("Full Name: ");
                        String fn = sc.nextLine();
                        log.info("Email: ");
                        String em = sc.nextLine();
                        log.info("Password: ");
                        String pw = sc.nextLine();
                        userService.register(u, fn, em, pw);
                        break;

                    case 2:
                        log.info("Username: ");
                        String lu = sc.nextLine();
                        log.info("Password: ");
                        String lp = sc.nextLine();

                        if (userService.login(lu, lp)) {
                            loggedUser = lu;
                            log.info("Welcome " + loggedUser);
                        }
                        break;

                    case 3:
                        log.info("Enter username: ");
                        String fu = sc.nextLine();
                        userService.forgotPassword(fu);
                        break;

                    case 4:
                        log.info("Goodbye.");
                        return;

                    default:
                        log.warn("Invalid.");
                }

            } else {

                // ---------------------------
                // SOCIAL MENU
                // ---------------------------

                log.info("");
                log.info("*** Menu (" + loggedUser + ") ***");
                log.info("1. View Profile");
                log.info("2. Create Profile");
                log.info("3. Update Profile");
                log.info("4. Send Friend Request");
                log.info("5. View Pending Requests");
                log.info("6. Accept Friend Request");
                log.info("7. Follow User");
                log.info("8. Unfollow User");
                log.info("9. View Followers");
                log.info("10. View Following");
                log.info("11. Create Post");
                log.info("12. View All Posts");
                log.info("13. View Personalized Feed");
                log.info("14. Like a Post");
                log.info("15. Comment on a Post");
                log.info("16. Logout");

                log.info("Choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        userService.viewProfile(loggedUser);
                        break;

                    case 2:
                        log.info("Bio: ");
                        String bio = sc.nextLine();
                        log.info("Location: ");
                        String loc = sc.nextLine();
                        userService.createProfile(loggedUser, bio, loc);
                        break;

                    case 3:
                        log.info("New Bio: ");
                        String newBio = sc.nextLine();
                        log.info("New Location: ");
                        String newLoc = sc.nextLine();
                        userService.updateProfile(loggedUser, newBio, newLoc);
                        break;

                    case 4:
                        log.info("Username to send request: ");
                        String frUser = sc.nextLine();
                        friendService.sendFriendRequest(loggedUser, frUser);
                        break;

                    case 5:
                        friendService.viewPendingRequests(loggedUser);
                        break;

                    case 6:
                        System.out.print("Request ID: ");
                        int reqId = sc.nextInt();
                        sc.nextLine();
                        friendService.acceptFriendRequest(loggedUser, reqId);
                        break;

                    case 7:
                        log.info("Follow username: ");
                        String fUser = sc.nextLine();
                        followService.followUser(loggedUser, fUser);
                        break;

                    case 8:
                        log.info("Unfollow username: ");
                        String ufUser = sc.nextLine();
                        followService.unfollowUser(loggedUser, ufUser);
                        break;

                    case 9:
                        followService.showFollowers(loggedUser);
                        break;

                    case 10:
                        followService.showFollowing(loggedUser);
                        break;

                    case 11:
                        log.info("Write post: ");
                        String txt = sc.nextLine();
                        postService.createPost(loggedUser, txt);
                        break;

                    case 12:
                        postService.viewFeed();
                        break;

                    case 13:
                        postService.viewPersonalizedFeed(loggedUser);
                        break;

                    case 14:
                        log.info("Post ID: ");
                        String likeId = sc.nextLine();
                        postService.likePost(likeId, loggedUser);
                        break;

                    case 15:
                        log.info("Post ID: ");
                        String cid = sc.nextLine();
                        log.info("Comment: ");
                        String ct = sc.nextLine();
                        postService.commentOnPost(cid, loggedUser, ct);
                        break;

                    case 16:
                        log.info("Logged out");
                        loggedUser = null;
                        break;

                    default:
                        log.warn("Invalid");
                }
            }
        }
    }
}
