/*jslint browser: true*/
/*global $, jQuery, alert*/
$(document).ready(function () {
    'use strict';
    $('#enter-to-register-button').click(function () {
        $('#enter-form').css('display', 'none');
        $('#register-form').css('display', 'flex');
    });
    $('.main-site-link a').click(function () {
        $('#enter-form').css('display', 'flex');
        $('#register-form').css('display', 'none');
    });
});