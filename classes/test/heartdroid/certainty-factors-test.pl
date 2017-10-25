xtype [name: numeric,
       base: numeric,
       length: 10,
       desc: 'Numeric value',
       domain: [-2147483647 to 2147483647]
      ].

xtype [name: case,
       base: symbolic,
       desc: 'Symbolic value',
       domain: [default, positive, negative, toobig, toosmall]
      ].

%%%%%%%%%%%%%%%%%%%%%%%%% ATTRIBUTES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%

xattr [name: x,
       abbrev: x,
       class: simple,
       type: numeric
      ].

xattr [name: switch,
       abbrev: s,
       class: simple,
       type: case
      ].


%%%%%%%%%%%%%%%%%%%%%%%% TABLE SCHEMAS DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%

xschm 'Test': [s] ==> [x].

%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

xrule 'Test'/0:
    [s eq default] ==> [x set 1].
xrule 'Test'/1:
    [s eq positive] ==> [x set 1]. # 0.5
xrule 'Test'/2:
    [s eq negative] ==> [x set 1]. # -0.5
xrule 'Test'/3:
    [s eq toobig] ==> [x set 1]. # 143
xrule 'Test'/4:
    [s eq toosmall] ==> [x set 1]. # -42


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%