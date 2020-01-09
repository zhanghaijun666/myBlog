/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
(function(global, factory) { /* global define, require, module */

    /* AMD */ if (typeof define === 'function' && define.amd)
        define(["protobufjs/light"], factory);

    /* CommonJS */ else if (typeof require === 'function' && typeof module === 'object' && module && module.exports)
        module.exports = factory(require("protobufjs/light"));

})(this, function($protobuf) {
    "use strict";

    var $root = ($protobuf.roots.BlogStore || ($protobuf.roots.BlogStore = new $protobuf.Root()))
    .setOptions({
      java_package: "com.blog.proto",
      java_outer_classname: "BlogStore"
    })
    .addJSON({
      Status: {
        values: {
          StatusDefault: 0,
          StatusActive: 1,
          StatusDeleted: 2
        }
      },
      ReturnCode: {
        values: {
          UNKNOWN_RETURN_CODE: 0,
          Return_OK: 1,
          Return_ERROR: 2,
          Return_USER_EXIST: 50,
          Return_PASSWORD_ERROR: 51,
          Return_USERNAME_OR_PASSWORD_IS_EMPTY: 52,
          Return_USER_EMPTY: 53,
          Return_NOT_YOURSELF: 54
        }
      },
      UserItem: {
        fields: {
          username: {
            type: "string",
            id: 1
          },
          nickname: {
            type: "string",
            id: 2
          },
          email: {
            type: "string",
            id: 3
          },
          phone: {
            type: "string",
            id: 4
          },
          birthday: {
            type: "int64",
            id: 5
          },
          status: {
            type: "Status",
            id: 20
          }
        }
      },
      UserList: {
        fields: {
          items: {
            rule: "repeated",
            type: "UserItem",
            id: 1
          }
        }
      }
    });

    return $root;
});
