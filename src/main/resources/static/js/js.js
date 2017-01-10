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
    $('.page-mask').click(function () {
        $('.settings').css('display', 'none');
        $('.page-mask').css('display', 'none');
    });
    $('.settings-opener').click(function () {
        $('.page-mask').css('display', 'block');
        $('.settings').css('display', 'block');
    });
    var user_nav_condition;
    $('.user-nav-opener').click(function () {
        if (user_nav_condition === 0 || user_nav_condition === undefined) {
            $('.user-nav').css('display', 'flex').css('opacity', '1');
            $('.user-nav-opener').css('background-color', '#F5F5F5');
            user_nav_condition = 1;
        } else if (user_nav_condition === 1) {
            $('.user-nav').css('display', 'none').css('opacity', '0');
            $('.user-nav-opener').css('background-color', 'transparent');
            user_nav_condition = 0;
        }
    }).hover(function () {
        $('.user-nav-opener').css('background-color', '#F5F5F5');
    }).mouseleave(function () {
        $('.user-nav-opener').css('background-color', 'white');
    });
    $('h4').click(function () {
        $(this).css('display', 'none');
        $(this).next().css('display', 'flex');
    });
    $('.task-add h2').click(function () {
        $(this).parent().prev().css('display', 'block');
        $(this).parent().css('display', 'none');
    });
    $('.category-add label').click(function () {
        $('.category-add label').css('display', 'none');
        $('.category-add input').css('display', 'block').focus();
    });
    $(document).mouseup(function (e) {
       if ($('.user-nav-opener').has(e.target).length === 0) {
           $('.user-nav').css('display', 'none').css('opacity', '0');
           $('.user-nav-opener').css('background-color', 'transparent');
           user_nav_condition = 0;
       }
        if ($('.task-details').parent().has(e.target).length === 0) {
            $('.task-details').css('display', 'none');
        }
        if ($('.category-add input').parent().has(e.target).length === 0) {
            $('.category-add input').css('display', 'none');
            $('.category-add label').css('display', 'block');
        }
    });
});