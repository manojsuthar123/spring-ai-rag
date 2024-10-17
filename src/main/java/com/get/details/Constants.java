package com.get.details;

public class Constants {

    public static final String DEFAULT_USER_TEXT_ADVISE = """
		Context information is below.
		---------------------
		{document_text}
		---------------------
		Given the context and provided history information and not prior knowledge,
		reply to the user comment. If the answer is not in the context, inform
		the user that you can't answer the question.
		""";
}
