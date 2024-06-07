'use strict';

Object.defineProperty(exports, '__esModule', {
    value: true
});

var _build = require('./build/js');

Object.keys(_build).forEach(function (key) {
    if (key === 'default' || key === '__esModule') {
        return;
    }

    Object.defineProperty(exports, key, {
        enumerable: true,
        get() {
            return _build[key];
        }
    });
});
