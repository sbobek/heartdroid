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

@Relation(name=describes, subject=user)
@Recommended
@Deprecated
@Number(10)
xschm 'FlagAnnotation': [s] ==> [r].

@Relation(name=interests, subject=system)
@Relation(name=realizes, subject=failure)
xschm 'DoubleAnnotation': [s] ==> [r].


%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

@Recommended
xrule 'FlagAnnotation'/0:
    [s eq add] ==> [r set x + y].
@Deprecated
xrule 'DoubleAnnotation'/0:
    [s eq precedence] ==> [r set x + y * y - z / z].
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%