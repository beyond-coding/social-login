package com.beyondcoding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;

    private ConnectionRepository connectionRepository;

    @Autowired
    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        // model.addAttribute("facebookProfile", facebook.userOperations()
        // .getUserProfile());
        // User userProfile = facebook.userOperations().getUserProfile();
        // raises the exception caused by the "bio" field.

        // ----- workaround until this problem is fixed in new Spring Social
        // release
        String[] fields = { "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education", "email", "favorite_athletes",
                "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type", "is_verified", "languages",
                "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political", "quotes", "payment_pricepoints",
                "relationship_status", "religion", "security_settings", "significant_other", "sports", "test_group", "timezone", "third_party_id",
                "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift", "website", "work" };
        User userProfile = facebook.fetchObject("me", User.class, fields);
        model.addAttribute("facebookProfile", userProfile);

        PagedList<Post> feed = facebook.feedOperations()
                                       .getFeed();
        model.addAttribute("feed", feed);

        return "hello";
    }

}
