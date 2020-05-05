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
      OwnerType: {
        values: {
          OwnerDefault: 0,
          User: 1,
          Group: 2
        }
      },
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
          RETURN_USER_EMAIL_EXIST: 54,
          RETURN_FILE_EXIST: 100
        }
      },
      Msg: {
        fields: {},
        nested: {
          MsgTypeEnum: {
            values: {
              MsgTypeDefault: 0,
              MsgOnline: 1,
              MsgOffline: 2,
              MsgText: 3
            }
          },
          MsgData: {
            fields: {
              msgId: {
                type: "int64",
                id: 1
              },
              msgType: {
                type: "MsgTypeEnum",
                id: 2
              },
              fromUserId: {
                type: "int32",
                id: 3
              },
              toType: {
                type: "OwnerType",
                id: 4
              },
              toId: {
                type: "int32",
                id: 5
              },
              msgText: {
                type: "string",
                id: 6
              },
              sendTime: {
                type: "int64",
                id: 10
              },
              ip: {
                type: "string",
                id: 11
              }
            }
          }
        }
      },
      StoreFile: {
        fields: {},
        nested: {
          StoreTypeEnum: {
            values: {
              StoreDefault: 0,
              Tree: 1,
              Blob: 2
            }
          },
          StoreTree: {
            fields: {
              storeType: {
                type: "StoreFile.StoreTypeEnum",
                id: 1
              },
              childItem: {
                rule: "repeated",
                type: "string",
                id: 2
              },
              ownerType: {
                type: "int32",
                id: 5
              },
              ownerId: {
                type: "int32",
                id: 6
              },
              fileName: {
                type: "string",
                id: 7
              },
              contentType: {
                type: "string",
                id: 8
              },
              fileSize: {
                type: "int64",
                id: 9
              },
              createTime: {
                type: "int64",
                id: 20
              },
              updateTime: {
                type: "int64",
                id: 21
              },
              committerId: {
                type: "int32",
                id: 22
              }
            }
          },
          StoreList: {
            fields: {
              items: {
                rule: "repeated",
                type: "StoreTree",
                id: 1
              },
              parentItem: {
                type: "StoreTree",
                id: 2
              }
            }
          }
        }
      },
      Label: {
        fields: {
          labelId: {
            type: "int32",
            id: 1
          },
          parenId: {
            type: "int32",
            id: 2
          },
          title: {
            type: "string",
            id: 3
          },
          description: {
            type: "string",
            id: 4
          },
          color: {
            type: "string",
            id: 5
          },
          status: {
            type: "Status",
            id: 6
          }
        }
      },
      LabelList: {
        fields: {
          items: {
            rule: "repeated",
            type: "Label",
            id: 1
          }
        }
      },
      LabelFile: {
        fields: {
          labelId: {
            type: "int32",
            id: 1
          },
          items: {
            rule: "repeated",
            type: "StoreFile.StoreTree",
            id: 2
          },
          status: {
            type: "Status",
            id: 3
          }
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
          },
          code: {
            type: "ReturnCode",
            id: 2
          },
          msg: {
            type: "string",
            id: 3
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
