/*jslint browser: true*/
/*global $, jQuery, alert*/
$(document).ready(function () {
    'use strict';
    $('#enter-to-register-button').click(function () {
        $('#enter-form').css('display', 'none');
        $('#register-form').css('display', 'flex');
    });
    $('.main-site-link').click(function () {
        $('#enter-form').css('display', 'flex');
        $('#register-form').css('display', 'none');
    });
    $('.task-details-button').click(function () {
        var this_task_details = $(this).parent().children('.task-details');
        if (this_task_details.css('display') === 'none') {
            this_task_details.css('display', 'block');
        } else {
            this_task_details.css('display', 'none');
        }
    });
    var user_nav_condition = 0;
    $('.user-nav-opener').click(function () {
        if (user_nav_condition === 0) {
            $('.user-nav').css('display', 'flex').css('opacity', '1');
            $('.user-nav-opener').css('background-color', '#F5F5F5');
            user_nav_condition = 1;
        } else {
            $('.user-nav').css('display', 'none').css('opacity', '0');
            $('.user-nav-opener').css('background-color', 'transparent');
            user_nav_condition = 0;
        }
    });
//    $('.main-header-site-link').mouseover(function () {
//        $(this).children().attr('src', "../static/img/site-logo-main-bright.png");
//    });
//    $('.main-header-site-link').mouseout(function () {
//        $(this).children().attr('src', "../static/img/site-logo-main.png");
//    });
//    $('.main-site-link').mouseover(function () {
//        $(this).children().attr('src', "../static/img/site-logo-main-bright.png");
//    });
//    $('.main-site-link').mouseout(function () {
//        $(this).children().attr('src', "../static/img/site-logo-main.png");
//    });
    $('h4').click(function () {
        $(this).css('display', 'none');
        $(this).next().css('display', 'flex');
    });
    $('.task-add h2').click(function () {
        $(this).parent().prev().css('display', 'block');
        $(this).parent().css('display', 'none');
    });
    $(document).mouseup(function (e) {
//        if ($('.user-nav-opener').has(e.target).length === 0) {
//            $('.user-nav').css('display', 'none').css('opacity', '0');
//            $('.user-nav-opener').css('background-color', 'transparent');
//        }
        if ($('.task-details').parent().has(e.target).length === 0) {
            $('.task-details').css('display', 'none');
        }
        if ($('.task-add').parent().has(e.target).length === 0) {
            $('.task-add').css('display', 'none');
            $('h4').css('display', 'block');
        }
    });

});
//
//.user-nav-opener-checkbox:checked ~ .user-nav {
//    display: flex;
//    opacity: 1;
//}