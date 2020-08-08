package com.animesh.roy.animesapiblog.ui.main.blog.viewmodel


import android.net.Uri
import com.animesh.roy.animesapiblog.models.BlogPost
import com.animesh.roy.animesapiblog.persistence.BlogQueryUtils.Companion.BLOG_FILTER_DATE_UPDATED
import com.animesh.roy.animesapiblog.persistence.BlogQueryUtils.Companion.BLOG_ORDER_DESC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getIsQueryExhausted(): Boolean {
    return getCurrentViewStateOrNew().blogFields.isQueryExhausted
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getFilter(): String {
    return getCurrentViewStateOrNew().blogFields.filter
        ?: BLOG_FILTER_DATE_UPDATED
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getOrder(): String {
    return getCurrentViewStateOrNew().blogFields.order
        ?: BLOG_ORDER_DESC
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getSearchQuery(): String {
    return getCurrentViewStateOrNew().blogFields.searchQuery
        ?: return ""
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getPage(): Int{
    return getCurrentViewStateOrNew().blogFields.page
        ?: return 1
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getSlug(): String{
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPost?.let {
            return it.slug
        }
    }
    return ""
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.isAuthorOfBlogPost(): Boolean{
    return getCurrentViewStateOrNew().viewBlogFields.isAuthorOfBlogPost
        ?: false
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let {
            return it
        }?: getDummyBlogPost()
    }
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getDummyBlogPost(): BlogPost {
    return BlogPost(-1, "" , "", "", "", 1, "")
}

@FlowPreview
@UseExperimental(ExperimentalCoroutinesApi::class)
fun BlogViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}






