xtype [name: switch_type,
       base: numeric,
       desc: 'Numeric value',
       domain: [1,2]
      ].

xtype [name: result_type,
       base: symbolic,
       desc: 'Symbolic value',
       domain: [simple, general]
      ].

%%%%%%%%%%%%%%%%%%%%%%%%% ATTRIBUTES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%

xattr [name: result,
       abbrev: r,
       class: simple,
       type: result_type
      ].


xattr [name: switch_simple,
       abbrev: ss,
       class: simple,
       type: switch_type
      ].

xattr [name: switch_general,
       abbrev: sg,
       class: general,
       type: switch_type
      ].


%%%%%%%%%%%%%%%%%%%%%%%% TABLE SCHEMAS DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%

xschm 'Test_simple': [ss] ==> [r].
xschm 'Test_general': [sg] ==> [r].

%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

xrule 'Test_simple'/0:
    [ss eq 3 * -3 + (4 * 2 + 2)] ==> [r set simple].


xrule 'Test_general'/1:
    [sg eq intersec(dom(sg),[1]) ] ==> [r set general].
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%