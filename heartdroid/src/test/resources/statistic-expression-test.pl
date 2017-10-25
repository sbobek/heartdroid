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

xschm 'Test_statistic': [ss] ==> [r].
xschm 'Test_parameters': [sg] ==> [r].

%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

xrule 'Test_statistic'/0:
    [min(ss, -10 to 0) eq 3 * -3 + (4 * 2 + 2)] ==> [r set min(ss, -10:1:0)].

xrule 'Test_parameters'/1:
    [sg eq{min 25% in -5 to 0} intersec(dom(sg),[1]) ] ==> [r set valat(r, -1)].
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%