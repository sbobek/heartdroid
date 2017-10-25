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
       abbrev: y,
       class: simple,
       type: numeric
      ].

xattr [name: z,
       abbrev: z,
       class: simple,
       type: numeric
      ].

xattr [name: result,
       abbrev: r,
       class: simple,
       type: numeric
      ].


xattr [name: switch,
       abbrev: s,
       class: simple,
       type: case
      ].


%%%%%%%%%%%%%%%%%%%%%%%% TABLE SCHEMAS DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%

xschm 'Test': [s] ==> [r].

%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

xrule 'Test'/0:
    [s eq add] ==> [r set x + y].
xrule 'Test'/1:
    [s eq subtract] ==> [r set x - y].
xrule 'Test'/2:
    [s eq multiply] ==> [r set x * y].
xrule 'Test'/3:
    [s eq divide] ==> [r set x / y].
xrule 'Test'/4:
    [s eq brackets] ==> [r set (x + y) * (x + z)].
xrule 'Test'/5:
    [s eq precedence] ==> [r set x + y * y - z / z].
xrule 'Test'/6:
    [s eq associavity] ==> [r set x - y - z + x + y + z].
xrule 'Test'/7:
    [s eq undefined] ==> [r set x - y - z + x + x ** y ** z + z].
xrule 'Test'/8:
    [s eq wacko] ==> [r set (x + (x + y * z)) * ((x - z) + y)].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%