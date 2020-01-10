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
          RETURN_OK: 1,
          RETURN_ERROR: 2,
          RETURN_USER_EXIST: 50,
          RETURN_USER_NAME_BLANK: 51,
          RETURN_USER_PASSWORD_BLANK: 52,
          RETURN_USER_PHONE_EXIST: 53,
          RETURN_USER_EMAIL_EXIST: 54
        }
      },
      Result: {
        fields: {
          code: {
            type: "ReturnCode",
            id: 1
          },
          msg: {
            type: "string",
            id: 2
          }
        }
      },
      ResultList: {
        fields: {
          esult: {
            rule: "repeated",
            type: "Result",
            id: 1
          }
        }
      },
      UserItem: {
        fields: {
          userId: {
            type: "int32",
            id: 1
          },
          username: {
            type: "string",
            id: 2
          },
          nickname: {
            type: "string",
            id: 3
          },
          email: {
            type: "string",
            id: 4
          },
          phone: {
            type: "string",
            id: 5
          },
          birthday: {
            type: "int64",
            id: 6
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
