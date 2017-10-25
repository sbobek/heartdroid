xtype [name: numeric,
       base: numeric,
       length: 10,
       desc: 'Numeric value',
       domain: [-2147483647 to 2147483647]
      ].

xtype [name: case,
       base: symbolic,
       desc: 'Symbolic value',
       domain: [add, subtract, multiply, divide, brackets, precedence, associavity, undefined, wacko]
      ].

%%%%%%%%%%%%%%%%%%%%%%%%% ATTRIBUTES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%

xattr [name: x,
       abbrev: x,
       class: simple,
       type: numeric
      ].

xattr [name: y,
       class: simple,
       type: numeric
      ].

xattr [name: z,
       class: simple,
       type: numeric
      ].


xattr [name: switch,
       class: simple,
       type: case
      ].


%%%%%%%%%%%%%%%%%%%%%%%% TABLE SCHEMAS DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%

xschm 'Test': [switch] ==> [z].

%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

xrule 'Test'/0:
    [switch eq add] ==> [z set x + y].
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%