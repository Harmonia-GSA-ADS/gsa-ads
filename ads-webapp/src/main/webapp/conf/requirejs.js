/**
 * Configure dependencies
 */

// Throws a "Read only." error, but disregard.
var require = {
    urlArgs: "_nc=" + (new Date()).getTime(), // debug
    baseUrl: 'js',
    paths: {
        text: 'lib/requirejs-text',
        bootstrap: 'lib/bootstrap.min'
    }
};