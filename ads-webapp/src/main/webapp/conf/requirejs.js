/**
 * Configure dependencies
 */

// Throws a "Read only." error, but disregard.
var require = {
    urlArgs: "_nc=" + (new Date()).getTime(), // debug
    baseUrl: 'js',
    paths: {
        jquery: 'lib/jquery-2.1.1.min',
        jqueryui: 'lib/jquery-ui.min',
        underscore: 'lib/underscore',
        backbone: 'lib/backbone',
        moment: 'lib/moment.min',
        handlebars: 'lib/Handlebars',
        text: 'lib/requirejs-text',
        bootstrap: '../lib/bootstrap/js/bootstrap.min'
    }
};