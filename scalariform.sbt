import scalariform.formatter.preferences._

scalariformPreferences :=
  scalariformPreferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AllowParamGroupsOnNewlines, true)
    .setPreference(CompactControlReadability, true)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
    .setPreference(NewlineAtEndOfFile, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
    .setPreference(PreserveSpaceBeforeArguments, true)