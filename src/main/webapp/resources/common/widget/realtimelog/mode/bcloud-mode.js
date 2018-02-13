/**
 * @file BCloud log mode for CodeMirror
 * @author liming16
 */
define(['codemirror'], function (CodeMirror) {

    'use strict';
    CodeMirror.defineMode('bcloud', function () {

        var words = {};
        
        function define(style, string) {
            var split = string.split(' ');
            for (var i = 0; i < split.length; i++) {
                words[split[i]] = style;
            }
        }

        // Atoms
        // define('atom', 'true false');

        // Keywords
        // define('keyword', 'if then do else elif while until for in esac fi fin '
        //   + 'fil done exit set unset export function break continue return eval printf shift');

        // Commands
        /* define('builtin', 'ab arch awk '
            + 'bash '
            + 'cat cc cd chown chgrp chmod chroot clear cp cal clock curl cut comake '
            + 'diff date df du dump'
            + 'echo egrep'
            + 'find free fgrep'
            + 'gawk gcc get git grep gzip'
            + 'ifconfig'
            + 'kill killall'
            + 'ls ln ll less'
            + 'make mkdir mv mount more'
            + 'node npm nc netstat'
            + 'openssl'
            + 'ping ps pwd'
            + 'restart rm rmdir rsync rpm'
            + 'sed service sh source sleep ssh start stop su sudo sort scp'
            + 'tee telnet top touch tar unrar tail'
            + 'unzip'
            + 'vi vim'
            + 'wall wc wget who which yes'
            + 'yum'
            + 'zip'
        );*/

        function tokenBase(stream, state) {
            if (stream.eatSpace()) {
                return null;
            }

            var sol = stream.sol();
            var ch = stream.next();

            /*if (ch === '\\') {
                stream.next();
                return null;
            }
            if (ch === '\'' || ch === '"' || ch === '`') {
                state.tokens.unshift(tokenString(ch));
                return tokenize(stream, state);
            }*/
            /*if (ch === '#') {
                if (sol && stream.eat('!')) {
                    stream.skipToEnd();
                    return 'meta'; // 'comment'?
                }
                stream.skipToEnd();
                return 'comment';
            }
            if (ch === '$') {
                state.tokens.unshift(tokenDollar);
                return tokenize(stream, state);
            }
            if (ch === '+' || ch === '=') {
                return 'operator';
            }
            if (ch === '-') {
                stream.eat('-');
                stream.eatWhile(/\w/);
                return 'attribute';
            }*/
            // 去除数字的匹配
            /*if (/\d/.test(ch)) {
                stream.eatWhile(/\d/);
                if (stream.eol() || !/\w/.test(stream.peek())) {
                    return 'number';
                }
            }*/
            // time
            /*if (ch === '[') {
                if (sol) {
                    stream.eatSpace();
                    stream.skipTo(']');
                    stream.eatSpace();
                    var nextString = stream.next();
                    if (nextString === 'Error') {
                        stream.skipToEnd();
                        return 'error';
                    } else if (nextString === 'Warning') {
                        stream.skipToEnd();
                        return 'warning';
                    } else {
                        stream.skipToEnd();
                        return 'info';
                    }
                }
            }*/
            // log level
            if (ch === '(' || ch === '[') {
                if (ch === '(') {
                    stream.skipTo(')');
                } else {
                    stream.skipTo(']');
                }
                var currentStr = stream.current();
                if (currentStr.endsWith('INFO')) {
                    stream.skipToEnd();
                    return 'info';
                } else if (currentStr.endsWith('WARNING')) {
                    stream.skipToEnd();
                    return 'warning';
                } else if (currentStr.endsWith('ERROR')) {
                    stream.skipToEnd();
                    return 'error';
                }
            } else if (ch === 'E') {
                stream.skipTo(':');
                var currentStr = stream.current();
                if (currentStr.endsWith('Error')) {
                    stream.skipToEnd();
                    return 'error';
                }
            } else if (ch === 'c') {
                stream.skipToEnd();
                var currentStr = stream.current();
                if (currentStr.indexOf('command not found') > -1) {
                    return 'error';
                }
            }
            stream.eatWhile(/[\w-]/);
            var cur = stream.current();
            if (stream.peek() === '=' && /\w+/.test(cur)) {
                return 'def';
            }
            return words.hasOwnProperty(cur) ? words[cur] : null;
        }

        function tokenString(quote) {
            return function (stream, state) {
                var next = false;
                var end = false;
                var escaped = false;
                while ((next = stream.next()) != null) {
                    if (next === quote && !escaped) {
                        end = true;
                        break;
                    }
                    if (next === '$' && !escaped && quote !== '\'') {
                        escaped = true;
                        stream.backUp(1);
                        state.tokens.unshift(tokenDollar);
                        break;
                    }
                    escaped = !escaped && next === '\\';
                }
                if (end || !escaped) {
                    state.tokens.shift();
                }
                return (quote === '`' || quote === ')' ? 'quote' : 'string');
            };
        }

        var tokenDollar = function (stream, state) {
            if (state.tokens.length > 1) {
                stream.eat('$');
            }

            var ch = stream.next();
            var hungry = /\w/;
            if (ch === '{') {
                hungry = /[^}]/;
            }
            if (ch === '(') {
                state.tokens[0] = tokenString(')');
                return tokenize(stream, state);
            }
            if (!/\d/.test(ch)) {
                stream.eatWhile(hungry);
                stream.eat('}');
            }
            state.tokens.shift();
            return 'def';
        };

        function tokenize(stream, state) {
            return (state.tokens[0] || tokenBase)(stream, state);
        }

        return {
            startState: function () {
                return {tokens: []};
            },
            token: function (stream, state) {
                return tokenize(stream, state);
            },
            lineComment: '#',
            fold: 'brace'
        };
    });

    CodeMirror.defineMIME('text/x-sh', 'shell');

});
